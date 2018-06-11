package com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers

import com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.breakByLength
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.audio.AudioAttachment
import com.pengrad.telegrambot.request.*
import org.joda.time.format.DateTimeFormat
import java.util.concurrent.TimeUnit

private val durationFormat = DateTimeFormat.forPattern("mm:ss")

class AudioHandler : PostHandler {
    override fun invoke(chatId: Long, post: Post, leftAttachments: MutableList<Attachment>): List<BaseRequest<*, *>> {
        return leftAttachments.filter {
            it is AudioAttachment
        }.let {
            val textBuilder = StringBuilder()
            it.mapNotNull {
                (it as AudioAttachment).let { audioAttachment ->
                    leftAttachments.remove(audioAttachment)
                    val title = (audioAttachment.audio.title ?: "Unnamed") + " - " + (audioAttachment.audio.artist
                        ?: "Unknown artist")
                    if (audioAttachment.audio.audioUrlCorrect) {
                        SendAudio(
                            chatId,
                            audioAttachment.audio.url
                        ).title(
                            title
                        ).duration(
                            audioAttachment.audio.duration
                        )
                    } else {
                        textBuilder.append("Song:\n$title : ${durationFormat.print(
                            TimeUnit.SECONDS.toMillis(
                                audioAttachment.audio.duration.toLong()
                            )
                        )}")
                        null
                    }
                }
            }.let {
                if (textBuilder.isNotEmpty()) {
                    it.plus(
                        textBuilder.toString().breakByLength().map {
                            SendMessage(
                                chatId,
                                it
                            )
                        }
                    )
                } else {
                    it
                }
            }
        }
    }
}
