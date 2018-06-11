package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.photo

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment

class PhotoAttachment(
    val photo: Photo
) : Attachment("photo")