package com.mytube.app.ui.screens

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.mytube.app.ui.viewmodel.VideoViewModel

@OptIn(UnstableApi::class)
@Composable
fun WatchScreen(
    videoId: String,
    viewModel: VideoViewModel,
    onBackClick: () -> Unit,
    onRelatedVideoClick: (String) -> Unit
) {
    val context = LocalContext.current
    val video = viewModel.getVideoById(videoId) ?: return
    val relatedVideos = viewModel.getRelatedVideos(videoId)
    var isSubscribed by remember { mutableStateOf(video.channel.isSubscribed) }
    var isLiked by remember { mutableStateOf(video.isLiked) }
    var likesCount by remember { mutableStateOf(video.likes) }
    var showCommentsDrawer by remember { mutableStateOf(false) }
    var newCommentText by remember { mutableStateOf("") }

    val exoPlayer = remember(video.videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(video.videoUrl))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F))
    ) {
        // ExoPlayer Video Player View
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(Color.Black)
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        useController = true
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .background(Color(0x88000000), CircleShape)
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        // Scrollable Video Details & Recommendations
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = video.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${video.viewsFormatted} · ${video.uploadedAt}",
                    color = Color(0xFFAAAAAA),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Channel Info & Subscribe Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = video.channel.avatar,
                            contentDescription = video.channel.name,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = video.channel.name,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${video.channel.subscribers} subscribers",
                                color = Color(0xFFAAAAAA),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Button(
                        onClick = {
                            isSubscribed = !isSubscribed
                            viewModel.toggleSubscribe(video.channel.id)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSubscribed) Color(0xFF272727) else Color.White,
                            contentColor = if (isSubscribed) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (isSubscribed) "Subscribed" else "Subscribe",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Bar: Like, Dislike, Share, Save
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Like Button
                    Surface(
                        color = Color(0xFF272727),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.clickable {
                            isLiked = !isLiked
                            likesCount += if (isLiked) 1 else -1
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                if (isLiked) Icons.Filled.ThumbUp else Icons.Filled.ThumbUpOffAlt,
                                contentDescription = "Like",
                                tint = if (isLiked) Color(0xFFFF0000) else Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "$likesCount",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Share Button
                    Surface(
                        color = Color(0xFF272727),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.clickable { /* Trigger Android Share Sheet */ }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Icon(Icons.Filled.Share, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Share", color = Color.White, fontSize = 12.sp)
                        }
                    }

                    // Save / Download Button
                    Surface(
                        color = Color(0xFF272727),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.clickable { /* Save */ }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Icon(Icons.Filled.BookmarkBorder, contentDescription = "Save", tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Comments Preview Box
                Surface(
                    color = Color(0xFF202020),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showCommentsDrawer = !showCommentsDrawer }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Comments ${video.commentsCount}",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color.White)
                        }
                        if (video.comments.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = video.comments.first().text,
                                color = Color(0xFFCCCCCC),
                                fontSize = 12.sp,
                                maxLines = 2
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Up Next",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Related Videos List
            items(relatedVideos) { related ->
                VideoCardItem(video = related, onClick = { onRelatedVideoClick(related.id) })
            }
        }
    }
}
