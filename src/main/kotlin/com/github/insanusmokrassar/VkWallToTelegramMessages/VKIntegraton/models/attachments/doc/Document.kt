package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.doc

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.AttachmentBase
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.common.Preview

class Document(
    val url: String,
    val title: String? = null,
    val type: String? = null,
    val ext: String = "gif",
    val size: Int? = null,
    val preview: Preview? = null
) : AttachmentBase() {
    val simpleUrl: String
        get() = "https://vk.com/doc${ownerId}_$id"
}