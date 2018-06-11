package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.doc

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment

class DocumentAttachment(
    val doc: Document
) : Attachment("doc")