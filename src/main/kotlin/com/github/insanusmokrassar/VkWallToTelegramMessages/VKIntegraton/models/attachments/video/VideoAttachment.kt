package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.video

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.VKMethodsHolder
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment

class VideoAttachment(
    var video: Video
) : Attachment("video") {
    override suspend fun prepareAttachment(vkMethodsHolder: VKMethodsHolder) {
        video = video.prepareAttachment(vkMethodsHolder) as? Video ?: video
    }
}