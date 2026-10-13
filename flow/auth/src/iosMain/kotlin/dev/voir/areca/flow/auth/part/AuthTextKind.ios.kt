package dev.voir.areca.flow.auth.part

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.input.PlatformImeOptions
import platform.UIKit.UITextContentTypeName
import platform.UIKit.UITextContentTypeNewPassword
import platform.UIKit.UITextContentTypeOneTimeCode
import platform.UIKit.UITextContentTypePassword
import platform.UIKit.UITextContentTypeUsername

/**
 * Names the `UITextContentType` for this kind of box.
 *
 * Both email kinds are `username`, which is what Password AutoFill pairs with the password box on a
 * sign-in or registration form; the email keyboard still comes from the keyboard type.
 *
 * @return Options with an explicit text content type.
 */
@OptIn(ExperimentalComposeUiApi::class)
internal actual fun AuthTextKind.platformImeOptions(): PlatformImeOptions? {
    val type = when (this) {
        AuthTextKind.Name -> UITextContentTypeName
        AuthTextKind.Email, AuthTextKind.NewEmail -> UITextContentTypeUsername
        AuthTextKind.Password -> UITextContentTypePassword
        AuthTextKind.NewPassword -> UITextContentTypeNewPassword
        AuthTextKind.OneTimeCode -> UITextContentTypeOneTimeCode
    }
    return PlatformImeOptions { textContentType(type) }
}
