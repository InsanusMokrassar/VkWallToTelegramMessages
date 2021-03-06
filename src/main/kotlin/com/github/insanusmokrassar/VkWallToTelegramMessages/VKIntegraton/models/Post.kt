package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.VKMethodsHolder
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.*
import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

class Post(
    val id: Long,
    val date: Long,// Unix timestamp
    @SerializedName("post_type")
    val postType: String,
    val text: String? = null,
    @SerializedName("owner_id")
    val ownerId: Long? = null,
    @SerializedName("from_id")
    val fromId: Long? = null,
    @SerializedName("reply_owner_id")
    val replyOwnerId: Long? = null,
    @SerializedName("reply_post_id")
    val replyPostId: Long? = null,
    @SerializedName("friends_only")
    val friendsOnly: Byte = 0,
    val attachments: List<Any>? = null,
    @SerializedName("copy_history")
    val copyHistory: List<Post>? = null,
    val is_pinned: Int = 0
) {
    val isPost: Boolean
        get() = postType == "post"
    val isCopy: Boolean
        get() = postType == "copy"
    val isReply: Boolean
        get() = postType == "reply"
    val isPostpone: Boolean
        get() = postType == "postpone"
    val isSuggest: Boolean
        get() = postType == "suggest"

    val isPinned: Boolean
        get() = is_pinned == 1

    val dateInMillis: Long
        get() = TimeUnit.SECONDS.toMillis(date)

    private var realAdaptedAttachments: List<Attachment>? = null

    val adaptedAttachments: List<Attachment>
        get() = realAdaptedAttachments ?: let {
            (attachments ?.map {
                it.asAttachment()
            } ?: emptyList()).also {
                realAdaptedAttachments = it.let {
                    list ->
                    text ?.let {
                        if (text.isEmpty()) {
                            null
                        } else {
                            list.plus(TextAttachment(it))
                        }
                    } ?: list
                }
            }
        }

    suspend fun prepareAttachments(vkMethodsHolder: VKMethodsHolder) {
        adaptedAttachments.forEach {
            it.prepareAttachment(vkMethodsHolder)
        }
        copyHistory ?. forEach {
            it.prepareAttachments(vkMethodsHolder)
        }
    }

    suspend fun flatAdaptedAttachments(): List<Attachment> {
        return copyHistory ?. flatMap {
            it.flatAdaptedAttachments()
        } ?.let {
            adaptedAttachments.plus(it)
        } ?: adaptedAttachments
    }
}

class PostsLists(count: Long, items: List<Post>) : Lists<Post>(count, items)

class PostsResponse(response: PostsLists) : Response<PostsLists>(response)
