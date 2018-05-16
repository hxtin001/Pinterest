class PinterestPage extends geb.Page {

    static at = { assert title.contains("Pinterest") }

    static content = {

        loginGmailBtn {$("button.GoogleConnectButton.active")}

    }
}
