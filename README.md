# payze-android-sdk
Payze Library for easy integration of payments into the Android app.

### Installation:

In the App module settings.gradle file, add the repository address to the repository block:

```groovy
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```

Add a dependency to our library in the app build.gradle file:
```groovy
dependencies {
    ...
    implementation 'com.github.payzeio:payze-android-sdk:0.1.1'
}
```

### Usage:
Using our library is pretty simple. Just create instance of Payze class with Activity context and call method 'pay'. This method takes card data, transactionID, success & error callbacks as parameters. all parameters are mandatory.
```kotlin
val payze = Payze(activityContext)
payze.pay(
    getCardData(),
    transactionID,
    onSuccess = {
        Log.d("TAG", "successfully payed")
    },
    onError = { errorCode, errorText -> 
        Log.d("TAG", "failure: $errorCode: $errorText")
    }
)

fun getCardData(): CardInfo {
    return CardInfo(
        number = "1234567890123456",
        cardHolder = "card holder",
        expirationDate = "10/25",
        securityNumber = "123",
    )
}
```

In case of some error, "onError" callback will be triggered with corresponding code and text.

Follow the instructions for transaction processing on our website https://payze.io/docs.

### Error codes:
Some error codes from library.
* 1001: No internet connection
* 1002: Unsuccessful Request, General
* 1003: Canceled card verification
* 1004: Transaction status is not Successful
* 1005: Unknown error


#### Example
A simple Android app using this sdk can be found here https://github.com/payzeio/payze-android-sdk-example
