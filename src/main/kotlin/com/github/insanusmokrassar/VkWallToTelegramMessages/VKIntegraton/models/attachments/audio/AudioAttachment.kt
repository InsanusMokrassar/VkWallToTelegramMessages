package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.audio

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment

class AudioAttachment(
    val audio: Audio
) : Attachment("audio")