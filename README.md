![eghl.png](http://e-ghl.com/assets/img/logo.png)

*Software Development Kit (SDK)* makes it easier to integrate your mobile application with eGHL Payment Gateway. This repository is intended as a technical guide for merchant developers and system integrators who are responsible for designing or programming the respective applications to integrate with Payment Gateway.

[eGHL](http://e-ghl.com) | [Wiki](https://bitbucket.org/eghl/android/wiki/Home) | [Downloads](https://bitbucket.org/eghl/android/downloads/?tab=tags) | [Follow eGHL Repos](https://bitbucket.org/eghl/follow)

****

# **Change Log** 
### [**v2.2.5](https://bitbucket.org/eghl/android/commits/tag/v2.2.5)
* Deprecated setExitTitle() and setExitMessage()
* Added ways to override strings messages of the SDK [for example](https://bitbucket.org/eghl/android/src/4121306cbecc13f85dad7a97fe6f620e22ac41b8/Demo/app/src/main/res/values/strings.xml?at=master&fileviewer=file-view-default#strings.xml-5,6,7,8,9,10,11)
* SDk will only return -6767 for SSL error

### [**v2.2.4](https://bitbucket.org/eghl/android/commits/tag/v2.2.5)


* Fix for issue #4: SDK crashing when MerchantReturnURL has a query strings

### [**v2.2.3](https://bitbucket.org/eghl/android/commits/tag/v2.2.3)
* Removed retry policy in all transactions


### [**v2.2.2](https://bitbucket.org/eghl/android/commits/tag/v2.2.2)
* Fixed issue #1 Crash when triggering capture


### [**v2.2.1](https://bitbucket.org/eghl/android/commits/tag/v2.2.1)
* Converted all query strings raw response into a JSON format
* Fixed the bug on backpress when the user went straight to the bank website via IssuingBank parameter and DD value on PaymentMethod


### [**v2.2.0](https://bitbucket.org/eghl/android/commits/tag/v2.2.0)
* TokenType can now be set in MasterpassParams.
* Added Capture transation
* Added way to override the payment gateway and password via Java Code in all EGHL.executeXXX() methods.

### [**v2.1.8](https://bitbucket.org/eghl/android/commits/tag/v2.1.8)
* added support to set the gateway in java instead of setting it in android manifest

### [**v2.1.7](https://bitbucket.org/eghl/android/commits/tag/v2.1.7)
* Added TRANSACTION_ERROR_IN_WEBVIEW result code when there’s an error in loading the page in the WebView 
* Fixed issue in executeQuery() in Kitkat 4.4.2
* Added setExitMessage() and setExitTitle() to the PaymentParams. This will override the default exit message.
* Changed the default message from "Pressing BACK button will...” to "Pressing EXIT button will…” 

### [**v2.1.6](https://bitbucket.org/eghl/android/commits/tag/v2.1.6)
* added instant status instead of doing requery same like iOS behaviour

### [**v2.1.5](https://bitbucket.org/eghl/android/commits/tag/v2.1.5)
* added the following scenario:
	* if user press back button before eghl ladning page finish load, SDK would return Cancelled Status,
	* If user press back button after eghl landing page finish load, SDK will return exactly like when the user click cancel link at the bottom of eGHL landing page.

### [**v2.1.4](https://bitbucket.org/eghl/android/commits/tag/v2.1.4)
* Fixed on user able to terminate requery by tapping the back button
* Fixed on Merchant app receiving Pending status when user click cancel in eGHL Payment Page

### [**v2.1.0](https://bitbucket.org/eghl/android/commits/tag/v2.1.0)
* fixed unable to make payment when passing MerchantCallbackURL to SDK 
* fixed callback not trigger after transaction done
* added SDK timeout to prevent user from making payment at bank side after Transaction time limit reached 
* Added Masterpass integration