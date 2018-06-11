package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models

import com.github.insanusmokrassar.IObjectK.interfaces.IInputObject
import com.github.insanusmokrassar.IObjectK.interfaces.IObject
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.asAttachment
import com.google.gson.annotations.SerializedName
import javafx.geometry.Pos
import java.util.concurrent.TimeUnit

class Post(
    val id: Long,
    val date: Long,// Unix timestamp
    val text: String = "",
    @SerializedName("post_type")
    val postType: String,
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
    val attachments: List<Any>,
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
            attachments.map {
                it.asAttachment()
            }.also {
                realAdaptedAttachments = it
            }
        }
}

class PostsLists(count: Long, items: List<Post>) : Lists<Post>(count, items)

class PostsResponse(response: PostsLists) : Response<PostsLists>(response)
