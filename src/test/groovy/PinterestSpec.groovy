import geb.error.GebException
import geb.spock.GebReportingSpec
import groovy.util.logging.*
import geb.driver.CachingDriverFactory
import org.apache.log4j.Logger
import org.openqa.selenium.StaleElementReferenceException
import spock.lang.*

@Log4j
class PinterestSpec extends GebReportingSpec {


    @Shared
            jutils = new JSONUtils()

    private static final Logger logger = Logger.getLogger("ExternalAppLogger")

    def setup() {
        CachingDriverFactory.clearCache()
        browser.config.autoClearCookies = true
        driver.manage().window().maximize()
    }

    def cleanup() {
        browser.close()
    }

    @Unroll
    "Pinterest pin"() {

        log.info("--------------------------------------------------------------------------------------")
        def urls = jutils.getConfig("WEB_URL")

        driver.switchTo().defaultContent()
        when:
        to PinterestPage
        log.info("Pinterest opened")

        String winHandleBefore = driver.getWindowHandle()
        String usernameVal = Utils.getUsername(usernameValue)
        String message
        // Set up anti-captcha and create mail box
        try {
            driver.executeScript('''return window.open("google.com", "_blank")''')
            for (String winHandle : driver.getWindowHandles()) {
                if (winHandle != winHandleBefore) {
                    driver.switchTo().window(winHandle)
                    driver.get("https://accounts.google.com/ServiceLogin")
                    Thread.sleep(5000)
                    when: "At google login page"
                    at GmailLoginPage
                    log.info("At gmail login page")
                    Utils.selectByValue($("input[type='email']"), usernameVal, "User name")
                    Utils.clickElement($("div#identifierNext span.RveJvd.snByac"), "Clicked next button", true)
                    Thread.sleep(3000)
                    Utils.selectByValue($("input[type='password']"), passwordValue, "Password")
                    Thread.sleep(1000)
                    Utils.clickElement($("div#passwordNext span.RveJvd.snByac"), "Clicked next button", true)
                    Thread.sleep(7000)
                }
            }
            driver.switchTo().window(winHandleBefore)
        } catch (Exception e) {
            driver.close()
        } catch (org.openqa.selenium.WebDriverException we) {
            log.error("WebDriver exception.")
            driver.close()
        }

        then:
        Utils.clickElement(loginGmailBtn, "Clicked Continue with Google button", true)
        Thread.sleep(15000)

        then:
//        try {
//            //$("#view_container div[role='button']").click()
//        } catch (StaleElementReferenceException se) {
//            log.warn("StaleElementReferenceException: element invisible")
//        } catch (groovy.lang.MissingMethodException me) {
//            log.warn(me.getMessage())
//        } catch (geb.waiting.WaitTimeoutException we) {
//            log.warn(we.getMessage())
//        } catch (geb.error.RequiredPageContentNotPresent re) {
//            log.warn(re.getMessage())
//        } catch (org.openqa.selenium.ElementNotVisibleException ee) {
//            log.warn(ee.getMessage())
//        } catch (org.openqa.selenium.WebDriverException wde) {
//            log.warn(wde.getMessage())
//        } catch (GebException ge) {
//            log.warn(ge.getMessage())
//        } catch (Exception e) {
//            log.warn("An error occur when we try to click element")
//        }

        then:
        try {
            driver.switchTo().window(winHandleBefore)
            def msg = ""
            msg = driver.executeScript('''return $(".NuxGenderStep__headerContent h2").innerHTML;''')
            if (msg.contains("describe yourself")) {
                $("span.NuxGenderStep__genderOptionTextNoHover")[0].click()
                Thread.sleep(1000)
            }
            Utils.clickElement($("#newUserLanguage"), language, "Language")
            Utils.clickElement($("button[type='submit']"), "", true)
            Thread.sleep(5000)
            $("button.NuxInterest").eachWithIndex{ entry, index ->
                if (index < 5) {
                    entry.click()
                }
            }
            Utils.clickElement($("button[type='submit']"), "", true)
            Thread.sleep(1000)
            Utils.clickElement($("button.NuxExtensionUpsell__optionalSkip"), "", true)
            Thread.sleep(1000)
            Utils.clickElement($("div[style='width: 284px;'] button[type='button']"), "Da hieu", true)
        } catch (StaleElementReferenceException se) {
            log.warn("StaleElementReferenceException: element invisible")
        } catch (GebException ge) {
            log.warn(ge.getMessage())
        } catch (groovy.lang.MissingMethodException me) {
            log.warn(me.getMessage())
        } catch (geb.waiting.WaitTimeoutException we) {
            log.warn(we.getMessage())
        } catch (geb.error.RequiredPageContentNotPresent re) {
            log.warn(re.getMessage())
        } catch (org.openqa.selenium.ElementNotVisibleException ee) {
            log.warn(ee.getMessage())
        } catch (org.openqa.selenium.WebDriverException wde) {
            log.warn(wde.getMessage())
        } catch (Exception e) {
            log.warn("An error occur when we try to click element")
        }

        then:
        driver.get("https://www.pinterest.com/${usernameVal}/pins/")
        Thread.sleep(10000)
        Utils.clickElement($("div.UserProfileContent div[role='button']"), "Clicked pin", true)
        Thread.sleep(1500)
        Utils.clickElement($("div[data-test-id='native-content-composer'] button[aria-selected='false']"), "Save from page", true)

        urls.each {
            Utils.selectByValue($("input[type='url']"), "${it}", "Url")
            Utils.clickElement($("div[style='max-height: 90vh;'] button[type='button']"), "Clicked done", true)
        }

        then: "Close"
        Thread.sleep(2000)
        driver.quit()

        where:
            usernameValue << jutils.get("USERNAME")
            passwordValue << jutils.get("PASSWORD")
            language << jutils.get("LANGUAGE")
    }

}