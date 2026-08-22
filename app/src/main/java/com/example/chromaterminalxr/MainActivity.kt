package com.example.chromaterminalxr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chromaterminalxr.ui.theme.ChromaTerminalXRTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChromaTerminalXRTheme {
                SpatialTerminalWindow()
            }
        }
    }
}

// Spatial Neon Color Palette
val NeonEmerald = Color(0xFF00FF9D)
val ElectricCyan = Color(0xFF00E5FF)
val DeepViolet = Color(0xFFB066FF)
val WarningGold = Color(0xFFFFC700)
val CriticalRed = Color(0xFFFF2A5F)
val DimSteel = Color(0xFF4A5568)
val DarkVoid = Color(0xFF0A0C14)

@Composable
fun SpatialTerminalWindow() {
    var commandText by remember { mutableStateOf("") }
    var showQuickBar by remember { mutableStateOf(true) }

    var terminalOutput by remember {
        mutableStateOf(
            listOf(
                "  ▲   ____ _   _ ____   ___  __  __    _  ",
                " ◄█► / ___| |_| |  _ \\ / _ \\|  \\/  |  / \\ ",
                "  ▼ | |   |  _  | |_) | | | | |\\/| | / _ \\",
                "    | |___| | | |  _ <| |_| | |  | |/ ___ \\",
                "     \\____|_| |_|_| \\_\\___/|_|  |_/_/   \\_\\",
                " --------------------------------------------------",
                "<SYS> Chroma Terminal spatial kernel loaded (v1.2.0-XR)",
                "<NET> Spatial Node: 127.0.0.1:8080 // Anchor: LOCKED",
                "<OK>  Neural gesture engine initialized",
                "<SYS> Use GUI quick actions above or type 'help'.",
                " --------------------------------------------------"
            )
        )
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    fun executeCommand(input: String) {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return

        val newHistory = terminalOutput.toMutableList()
        newHistory.add("chroma@xr:~$ $trimmed")

        val parts = trimmed.split(" ")
        when (parts[0].lowercase()) {
            "help" -> {
                newHistory.add("<SYS> Active Command Modules:")
                newHistory.add("  <OK>  sysinfo  - Query spatial runtime diagnostics")
                newHistory.add("  <NET> scan     - Probe local 3D spatial anchors")
                newHistory.add("  <WAR> clear    - Flush buffer stream")
                newHistory.add("  <SYS> echo     - Output text string to buffer")
            }
            "sysinfo" -> {
                newHistory.add("<SYS> [SPATIAL_DIAGNOSTICS]")
                newHistory.add("  <OK> OS Target: Android XR Spatial Platform")
                newHistory.add("  <OK> Surface Mode: Floating Window Panel")
                newHistory.add("  <OK> Memory Heap: Nominal (34MB allocated)")
                newHistory.add("  <NET> Anchor Latency: 1.2ms")
            }
            "scan" -> {
                newHistory.add("<NET> Scanning spatial environment for anchors...")
                newHistory.add("  <OK> [Anchor 0x1A] Center Window - LOCKED")
                newHistory.add("  <OK> [Anchor 0x1B] Orbiting Controller - SYNCHRONIZED")
                newHistory.add("<OK> Environment scan finished. 2 spatial anchors registered.")
            }
            "clear" -> {
                newHistory.clear()
            }
            "echo" -> {
                val message = parts.drop(1).joinToString(" ")
                newHistory.add("  $message")
            }
            else -> {
                newHistory.add("<ERR> Module '${parts[0]}' unknown. Enter 'help' for directory.")
            }
        }

        terminalOutput = newHistory
        commandText = ""

        coroutineScope.launch {
            if (terminalOutput.isNotEmpty()) {
                listState.animateScrollToItem(terminalOutput.size - 1)
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp),
        color = DarkVoid,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            // Dashboard Header & Visual Metrics Progress Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CHROMA//XR · KERNEL_SHELL",
                        color = ElectricCyan,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "HEAP LOAD ",
                            color = DimSteel,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        )
                        LinearProgressIndicator(
                            progress = { 0.35f },
                            modifier = Modifier
                                .width(80.dp)
                                .height(4.dp),
                            color = NeonEmerald,
                            trackColor = DimSteel.copy(alpha = 0.3f)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "[ ❖ ANCHOR: ACTIVE ]",
                        color = NeonEmerald,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = { showQuickBar = !showQuickBar },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = if (showQuickBar) "[ GUI: ON ]" else "[ GUI: OFF ]",
                            color = DeepViolet,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            HorizontalDivider(color = DeepViolet.copy(alpha = 0.4f), thickness = 1.dp)

            // Visual Quick-Action Buttons (Clickable GUI Chips)
            AnimatedVisibility(visible = showQuickBar) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick = { executeCommand("scan") },
                        label = { Text("🔍 Spatial Scan", color = ElectricCyan, fontSize = 11.sp) },
                        border = AssistChipDefaults.assistChipBorder(enabled = true, borderColor = ElectricCyan.copy(alpha = 0.5f))
                    )
                    AssistChip(
                        onClick = { executeCommand("sysinfo") },
                        label = { Text("📊 Diagnostics", color = NeonEmerald, fontSize = 11.sp) },
                        border = AssistChipDefaults.assistChipBorder(enabled = true, borderColor = NeonEmerald.copy(alpha = 0.5f))
                    )
                    AssistChip(
                        onClick = { executeCommand("help") },
                        label = { Text("❓ Help", color = DeepViolet, fontSize = 11.sp) },
                        border = AssistChipDefaults.assistChipBorder(enabled = true, borderColor = DeepViolet.copy(alpha = 0.5f))
                    )
                    AssistChip(
                        onClick = { executeCommand("clear") },
                        label = { Text("🧹 Clear", color = WarningGold, fontSize = 11.sp) },
                        border = AssistChipDefaults.assistChipBorder(enabled = true, borderColor = WarningGold.copy(alpha = 0.5f))
                    )
                }
            }

            // Main Terminal Output
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                items(terminalOutput) { line ->
                    Text(
                        text = formatChromaLine(line),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }
            }

            HorizontalDivider(color = DeepViolet.copy(alpha = 0.3f), thickness = 1.dp)

            // Command Prompt Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "chroma@xr:~$ ",
                    color = DeepViolet,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp
                )
                OutlinedTextField(
                    value = commandText,
                    onValueChange = { commandText = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { executeCommand(commandText) }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricCyan,
                        unfocusedBorderColor = DimSteel,
                        focusedTextColor = NeonEmerald,
                        unfocusedTextColor = NeonEmerald
                    )
                )
            }
        }
    }
}

// Token Highlighting System
fun formatChromaLine(line: String): AnnotatedString {
    return buildAnnotatedString {
        when {
            line.startsWith("<OK>") -> {
                withStyle(SpanStyle(color = NeonEmerald)) { append("<OK> ") }
                withStyle(SpanStyle(color = Color.White)) { append(line.removePrefix("<OK>")) }
            }
            line.startsWith("<SYS>") -> {
                withStyle(SpanStyle(color = ElectricCyan)) { append("<SYS> ") }
                withStyle(SpanStyle(color = Color.LightGray)) { append(line.removePrefix("<SYS>")) }
            }
            line.startsWith("<NET>") -> {
                withStyle(SpanStyle(color = DeepViolet)) { append("<NET> ") }
                withStyle(SpanStyle(color = Color.White)) { append(line.removePrefix("<NET>")) }
            }
            line.startsWith("<WAR>") -> {
                withStyle(SpanStyle(color = WarningGold)) { append("<WAR> ") }
                withStyle(SpanStyle(color = WarningGold)) { append(line.removePrefix("<WAR>")) }
            }
            line.startsWith("<ERR>") -> {
                withStyle(SpanStyle(color = CriticalRed)) { append("<ERR> ") }
                withStyle(SpanStyle(color = CriticalRed)) { append(line.removePrefix("<ERR>")) }
            }
            line.startsWith("chroma@xr:~$") -> {
                withStyle(SpanStyle(color = DeepViolet)) { append("chroma@xr:~$") }
                withStyle(SpanStyle(color = ElectricCyan)) { append(line.removePrefix("chroma@xr:~$")) }
            }
            else -> {
                withStyle(SpanStyle(color = NeonEmerald)) { append(line) }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 500)
@Composable
fun SpatialTerminalPreview() {
    ChromaTerminalXRTheme {
        SpatialTerminalWindow()
    }
}