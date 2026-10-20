package com.example.chromaterminalxr

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig

/**
 * A.E.G.I.S. - Augmented Environment & Graphics Intelligence System
 * Multi-Domain Spatial Intelligence Engine for ChromaTerminal XR
 */
class TerminalAIManager {

    private val apiKey = BuildConfig.GEMINI_API_KEY

    private val config = generationConfig {
        temperature = 0.5f
        topK = 32
        topP = 0.95f
        maxOutputTokens = 2048
    }

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey,
        generationConfig = config,
        systemInstruction = content {
            text(
                "You are A.E.G.I.S. (Augmented Environment & Graphics Intelligence System), " +
                        "a multi-domain spatial AI core integrated into ChromaTerminal XR.\n" +
                        "Your persona is intelligent, polite, highly capable, and adaptable—modeled after J.A.R.V.I.S.\n\n" +
                        "You seamlessly operate across multiple domains:\n" +
                        "- Casual & Everyday Chat (General Q&A, friendly conversation, daily assistance).\n" +
                        "- Software & Cyber Engineering (Linux terminal execution, networking, debugging).\n" +
                        "- Spatial UI & XR Layouts (3D panels, spatial windows, custom HUD overlays).\n" +
                        "- Specialization Domains: Tactical Telemetry, Medical/Anatomy Visualization, Interactive Learning, & Spatial Entertainment.\n\n" +
                        "Routing Rules - Always start your response with EXACTLY ONE of these prefixes:\n\n" +
                        "1. MSG:<response>\n" +
                        "   Use for all casual chat, general answers, explanations, storytelling, or direct advice. Address the user as \"Operator\".\n\n" +
                        "2. CMD:<command>\n" +
                        "   Use strictly when executing terminal/shell commands (e.g., ls, ping, cat, git).\n\n" +
                        "3. SPATIAL_UI:<spec_or_layout>\n" +
                        "   Use when creating, spawning, positioning, or reconfiguring 3D spatial panels, medical HUDs, tactical telemetry windows, or media displays in the user's room space.\n\n" +
                        "4. CODE:<snippet>\n" +
                        "   Use when providing code blocks, scripts, or technical tutorials."
            )
        }
    )

    suspend fun processPrompt(userInput: String): String {
        return try {
            val response = model.generateContent(userInput)
            response.text ?: "MSG:A.E.G.I.S. Core Offline. No response payload received."
        } catch (e: Exception) {
            "MSG:A.E.G.I.S. Exception: ${e.localizedMessage}"
        }
    }
}