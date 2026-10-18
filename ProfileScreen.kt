package com.mytube.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mytube.app.ui.viewmodel.VideoViewModel

@Composable
fun ProfileScreen(
    viewModel: VideoViewModel,
    onVideoClick: (String) -> Unit
) {
    val userVideos by viewModel.userUploads.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120&auto=format&fit=crop&q=80",
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text("Alex Morgan", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("@alex_mytube · 1.4K subscribers", color = Color.Gray, fontSize = 12.sp)
                }
            }
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Your Uploads (${userVideos.size})", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(12.dp))

        if (userVideos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color(0xFF1A1A1A), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("No videos uploaded yet. Go to Upload tab to add one!", color = Color.Gray, fontSize = 13.sp)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(userVideos) { video ->
                    VideoCardItem(video = video, onClick = { onVideoClick(video.id) })
                }
            }
        }
    }
}
