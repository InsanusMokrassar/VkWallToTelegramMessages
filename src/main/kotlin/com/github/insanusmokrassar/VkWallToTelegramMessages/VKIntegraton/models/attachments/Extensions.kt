package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments

import com.github.insanusmokrassar.IObjectKRealisations.*
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.doc.DocumentAttachment
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.photo.PhotoAttachment
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.video.VideoAttachment

private val attachmentsMap = mapOf(
    "photo" to PhotoAttachment::class.java,
    "doc" to DocumentAttachment::class.java,
    "video" to VideoAttachment::class.java
)

@Throws(UnsupportedAttachmentException::class)
fun Any.asAttachment(): Attachment {
    val asIObject = toIObject()
    return asIObject.toObject(
        attachmentsMap[asIObject.toObject(Attachment::class.java).type]
            ?: throw UnsupportedAttachmentException("Object is unsupported attachment")
    )
}
