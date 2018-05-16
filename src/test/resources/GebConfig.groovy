/*
	This is the Geb configuration file.

	See: http://www.gebish.org/manual/current/configuration.html
*/


import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService
import org.openqa.selenium.chrome.ChromeOptions

File findDriverExecutable() {
    return Utils.getDriver()
}

driver = {
    ChromeOptions options = new ChromeOptions()
    options.addArguments("test-type")
    options.addArguments("start-maximized")
    options.addArguments("disable-infobars")
    options.addArguments("--disable-web-security")
    ChromeDriverService service = new ChromeDriverService.Builder()
            .usingAnyFreePort()
            .usingDriverExecutable(findDriverExecutable())
            .build()
    return new ChromeDriver(service, options)
}


waiting {
    timeout = 60
    retryInterval = 1.0
    includeCauseInMessage = true
}

reportsDir = "target/reports"

baseUrl = "https://www.pinterest.com/"