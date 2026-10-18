package com.mytube.app.data.model

data class Channel(
    val id: String,
    val name: String,
    val handle: String,
    val avatar: String,
    val subscribers: String,
    val isVerified: Boolean = false,
    val isSubscribed: Boolean = false
)

data class Comment(
    val id: String,
    val author: String,
    val handle: String,
    val avatar: String,
    val timestamp: String,
    val text: String,
    val likes: Int = 0
)

data class Video(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val duration: String,
    val views: Long,
    val viewsFormatted: String,
    val uploadedAt: String,
    val category: String,
    val channel: Channel,
    val likes: Int,
    val isLiked: Boolean = false,
    val commentsCount: Int = 0,
    val comments: List<Comment> = emptyList(),
    val tags: List<String> = emptyList()
)
