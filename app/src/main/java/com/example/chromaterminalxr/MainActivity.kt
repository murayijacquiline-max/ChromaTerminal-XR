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
// --- XR Subspace & GLTF Imports ---
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.SpatialGltfModel
import androidx.xr.compose.subspace.SpatialGltfModelSource
import androidx.xr.compose.subspace.rememberSpatialGltfModelState
import androidx.xr.compose.subspace.ExperimentalSpatialGltfModelApi
import androidx.xr.compose.subspace.layout.SubspaceModifier
import com.example.chromaterminalxr.ui.theme.ChromaTerminalXRTheme
import kotlinx.coroutines.launch
import java.nio.file.Paths

@OptIn(ExperimentalSpatialGltfModelApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChromaTerminalXRTheme {
                // XR Subspace container houses both your 2D terminal and 3D companion
                Subspace {
                    SpatialPanel {
                        SpatialTerminalWindow()
                    }

                    // Renders your animated 3D anime companion model in spatial view
                    val modelState = rememberSpatialGltfModelState(
                        source = SpatialGltfModelSource.fromPath(Paths.get("models/anime_companion.glb"))
                    )

                    SpatialGltfModel(
                        state = modelState,
                        modifier = SubspaceModifier
                    )
                }
            }
        }
    }
}

// Color Theme Palettes
enum class TerminalTheme(
    val primary: Color,
    val secondary: Color,
    val accent: Color,
    val bg: Color
) {
    CYAN(Color(0xFF00FF9D), Color(0xFF00E5FF), Color(0xFFB066FF), Color(0xFF0A0C14)),
    AMBER(Color(0xFFFFB000), Color(0xFFFFD566), Color(0xFFFF7700), Color(0xFF140D04)),
    MATRIX(Color(0xFF00FF66), Color(0xFF33FF88), Color(0xFF00AA44), Color(0xFF041008)),
    VIOLET(Color(0xFFD477FF), Color(0xFF00E5FF), Color(0xFFFF44AA), Color(0xFF11081A))
}

@Composable
fun SpatialTerminalWindow() {
    var commandText by remember { mutableStateOf("") }
    var showQuickBar by remember { mutableStateOf(true) }
    var activeTheme by remember { mutableStateOf(TerminalTheme.CYAN) }
    var currentDirectory by remember { mutableStateOf("/home/chroma") }

    // --- A.E.G.I.S. AI Core & Coroutine Scope ---
    val aiManager = remember { TerminalAIManager("YOUR_API_KEY_HERE") }
    val coroutineScope = rememberCoroutineScope()

    // Mock Virtual File System Structure
    val virtualFiles = mapOf(
        "/home/chroma/sys_status.log" to "[LOG: 2026-08-22] All spatial anchors calibrated. Latency: 1.1ms.",
        "/home/chroma/xr_nodes.conf" to "node_0=0x1A_CENTER\nnode_1=0x1B_ORBITAL\nmode=HYBRID_SPATIAL",
        "/home/chroma/readme.txt" to "Welcome to ChromaTerminal XR. Floating 3D Shell built with Jetpack Compose."
    )

    var terminalOutput by remember {
        mutableStateOf(
            listOf(
                "  ▲   ____ _   _ ____   ___  __  __    _  ",
                " ◄█► / ___| |_| |  _ \\ / _ \\|  \\/  |  / \\ ", 
                "  ▼ | |   |  _  | |_) | | | | |\\/| | / _ \\",
                "    | |___| | | |  _ <| |_| | |  | |/ ___ \\",
                "     \\____|_| |_|_| \\_\\___/|_|  |_/_/   \\_\\",
                " --------------------------------------------------",
                "<SYS> Chroma Terminal spatial kernel loaded (v1.3.0-XR)",
                "<NET> A.E.G.I.S. AI Core Online // Type 'help' or chat naturally",
                " --------------------------------------------------"
            )
        )
    }

    val listState = rememberLazyListState()

    fun executeCommand(input: String) {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return

        val newHistory = terminalOutput.toMutableList()
        newHistory.add("chroma@xr:$currentDirectory$ $trimmed")

        val parts = trimmed.split("\\s+".toRegex())
        when (parts[0].lowercase()) {
            "help" -> {
                newHistory.add("<SYS> Available Command Modules:")
                newHistory.add("  <OK>  ls           - List files in virtual directory")
                newHistory.add("  <OK>  cat <file>   - Read contents of a file")
                newHistory.add("  <OK>  pwd          - Output current spatial working directory")
                newHistory.add("  <NET> sysinfo      - Display XR system performance metrics")
                newHistory.add("  <NET> theme <name> - Switch theme: cyan | amber | matrix | violet")
                newHistory.add("  <WAR> clear        - Flush terminal screen output buffer")
                newHistory.add("  <SYS> (Any other text is automatically processed by A.E.G.I.S. AI)")

                terminalOutput = newHistory
                commandText = ""
            }
            "ls" -> {
                newHistory.add("<OK> Directory contents of $currentDirectory:")
                virtualFiles.keys.forEach { path ->
                    val filename = path.substringAfterLast("/")
                    newHistory.add("  -rw-r--r-- 1 chroma xr 1024 $filename")
                }
                terminalOutput = newHistory
                commandText = ""
            }
            "cat" -> {
                if (parts.size < 2) {
                    newHistory.add("<ERR> Usage: cat <filename>")
                } else {
                    val targetPath = "$currentDirectory/${parts[1]}"
                    val content = virtualFiles[targetPath]
                    if (content != null) {
                        content.split("\n").forEach { line -> newHistory.add("  $line") }
                    } else {
                        newHistory.add("<ERR> File not found: ${parts[1]}")
                    }
                }
                terminalOutput = newHistory
                commandText = ""
            }
            "pwd" -> {
                newHistory.add("<OK> $currentDirectory")
                terminalOutput = newHistory
                commandText = ""
            }
            "theme" -> {
                if (parts.size < 2) {
                    newHistory.add("<SYS> Current theme: ${activeTheme.name}. Options: cyan, amber, matrix, violet")
                } else {
                    when (parts[1].lowercase()) {
                        "amber" -> { activeTheme = TerminalTheme.AMBER; newHistory.add("<OK> Theme changed to AMBER") }
                        "matrix" -> { activeTheme = TerminalTheme.MATRIX; newHistory.add("<OK> Theme changed to MATRIX") }
                        "cyan" -> { activeTheme = TerminalTheme.CYAN; newHistory.add("<OK> Theme changed to CYAN") }
                        "violet" -> { activeTheme = TerminalTheme.VIOLET; newHistory.add("<OK> Theme changed to VIOLET") }
                        else -> newHistory.add("<ERR> Unknown theme. Available: cyan, amber, matrix, violet")
                    }
                }
                terminalOutput = newHistory
                commandText = ""
            }
            "sysinfo" -> {
                newHistory.add("<SYS> [SPATIAL_DIAGNOSTICS]")
                newHistory.add("  <OK> Target: Android XR Spatial Platform")
                newHistory.add("  <OK> Memory Heap: 38MB / Active Palette: ${activeTheme.name}")
                newHistory.add("  <NET> FPS Target: 60 FPS // Latency: 1.1ms")
                terminalOutput = newHistory
                commandText = ""
            }
            "clear" -> {
                newHistory.clear()
                terminalOutput = newHistory
                commandText = ""
            }
            else -> {
                newHistory.add("<SYS> A.E.G.I.S. processing query...")
                terminalOutput = newHistory
                commandText = ""

                coroutineScope.launch {
                    val aiResponse = aiManager.processTerminalCommand(trimmed)
                    val updatedHistory = terminalOutput.toMutableList()

                    when {
                        aiResponse.startsWith("CMD:") -> {
                            updatedHistory.add("<OK> [A.E.G.I.S. CMD] ${aiResponse.removePrefix("CMD:")}")
                        }
                        aiResponse.startsWith("SPATIAL_UI:") -> {
                            updatedHistory.add("<NET> [A.E.G.I.S. UI] ${aiResponse.removePrefix("SPATIAL_UI:")}")
                        }
                        aiResponse.startsWith("CODE:") -> {
                            updatedHistory.add("<SYS> [A.E.G.I.S. CODE]")
                            aiResponse.removePrefix("CODE:").split("\n").forEach { line ->
                                updatedHistory.add("  $line")
                            }
                        }
                        else -> {
                            val cleanMsg = aiResponse.removePrefix("MSG:")
                            updatedHistory.add("<SYS> [A.E.G.I.S.] $cleanMsg")
                        }
                    }
                    terminalOutput = updatedHistory
                }
            }
        }

        coroutineScope.launch {
            if (terminalOutput.isNotEmpty()) {
                listState.animateScrollToItem(terminalOutput.size - 1)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize().padding(6.dp),
        color = activeTheme.bg,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CHROMA//XR · KERNEL_SHELL",
                        color = activeTheme.secondary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "HEAP LOAD ",
                            color = Color.Gray,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        )
                        LinearProgressIndicator(
                            progress = { 0.28f },
                            modifier = Modifier.width(80.dp).height(4.dp),
                            color = activeTheme.primary,
                            trackColor = Color.Gray.copy(alpha = 0.3f)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "[ ❖ ANCHOR: ACTIVE ]",
                        color = activeTheme.primary,
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
                            color = activeTheme.accent,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            HorizontalDivider(color = activeTheme.accent.copy(alpha = 0.4f), thickness = 1.dp)

            AnimatedVisibility(visible = showQuickBar) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick = { executeCommand("ls") },
                        label = { Text("📁 Files (ls)", color = activeTheme.secondary, fontSize = 11.sp) }
                    )
                    AssistChip(
                        onClick = { executeCommand("sysinfo") },
                        label = { Text("📊 Diagnostics", color = activeTheme.primary, fontSize = 11.sp) }
                    )
                    AssistChip(
                        onClick = {
                            val nextTheme = when(activeTheme) {
                                TerminalTheme.CYAN -> "amber"
                                TerminalTheme.AMBER -> "matrix"
                                TerminalTheme.MATRIX -> "violet"
                                TerminalTheme.VIOLET -> "cyan"
                            }
                            executeCommand("theme $nextTheme")
                        },
                        label = { Text("🎨 Palette (${activeTheme.name})", color = activeTheme.accent, fontSize = 11.sp) }
                    )
                    AssistChip(
                        onClick = { executeCommand("clear") },
                        label = { Text("🧹 Clear", color = Color(0xFFFFC700), fontSize = 11.sp) }
                    )
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).fillMaxWidth().padding(vertical = 4.dp)
            ) {
                items(terminalOutput) { line ->
                    Text(
                        text = formatLine(line, activeTheme),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }
            }

            HorizontalDivider(color = Color(0xFF00FF66).copy(alpha = 0.4f))

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "chroma@xr:$currentDirectory$ ",
                    color = activeTheme.accent,
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
                        focusedBorderColor = activeTheme.secondary,
                        unfocusedBorderColor = Color.DarkGray,
                        focusedTextColor = activeTheme.primary,
                        unfocusedTextColor = activeTheme.primary
                    )
                )
            }
        }
    }
}

fun formatLine(line: String, theme: TerminalTheme): AnnotatedString {
    return buildAnnotatedString {
        when {
            line.startsWith("<OK>") -> {
                withStyle(SpanStyle(color = theme.primary)) { append("<OK> ") }
                withStyle(SpanStyle(color = Color.White)) { append(line.removePrefix("<OK>")) }
            }
            line.startsWith("<SYS>") -> {
                withStyle(SpanStyle(color = theme.secondary)) { append("<SYS> ") }
                withStyle(SpanStyle(color = Color.LightGray)) { append(line.removePrefix("<SYS>")) }
            }
            line.startsWith("<NET>") -> {
                withStyle(SpanStyle(color = theme.accent)) { append("<NET> ") }
                withStyle(SpanStyle(color = Color.White)) { append(line.removePrefix("<NET>")) }
            }
            line.startsWith("<ERR>") -> {
                withStyle(SpanStyle(color = Color(0xFFFF2A5F))) { append("<ERR> ") }
                withStyle(SpanStyle(color = Color(0xFFFF2A5F))) { append(line.removePrefix("<ERR>")) }
            }
            else -> {
                withStyle(SpanStyle(color = theme.primary)) { append(line) }
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
