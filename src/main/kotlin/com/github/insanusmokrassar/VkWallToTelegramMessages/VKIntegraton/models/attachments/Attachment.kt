package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.VKMethodsHolder

open class Attachment(
    val type: String
) {
    open suspend fun prepareAttachment(vkMethodsHolder: VKMethodsHolder) {  }
}
