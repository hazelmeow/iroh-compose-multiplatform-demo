package com.example.irohcomposemultiplatform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import irohcomposemultiplatform.composeapp.generated.resources.Res
import irohcomposemultiplatform.composeapp.generated.resources.arrow_downward_24px
import irohcomposemultiplatform.composeapp.generated.resources.arrow_upward_24px
import irohcomposemultiplatform.composeapp.generated.resources.cell_tower_24px
import irohcomposemultiplatform.composeapp.generated.resources.connections_label
import irohcomposemultiplatform.composeapp.generated.resources.content_copy_24px
import irohcomposemultiplatform.composeapp.generated.resources.copy_button_description
import irohcomposemultiplatform.composeapp.generated.resources.home_relay_label
import irohcomposemultiplatform.composeapp.generated.resources.network_node_24px
import irohcomposemultiplatform.composeapp.generated.resources.node_id_label
import irohcomposemultiplatform.composeapp.generated.resources.p2p_24px
import irohcomposemultiplatform.composeapp.generated.resources.received_label
import irohcomposemultiplatform.composeapp.generated.resources.sent_label
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import uniffi.irohcompose.CoreException
import uniffi.irohcompose.Model
import uniffi.irohcompose.NodeModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
        ) {
            Status()
            Inputs()
        }
    }
}

@Composable
fun Status(
    viewModel: CoreViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsState()

    state?.let {
        Status(it)
    } ?: run {
        CircularProgressIndicator()
    }
}

@Composable
fun Status(state: Model) {
    Card(modifier = Modifier.padding(16.dp)) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Node Status",
                style = MaterialTheme.typography.titleLarge,
            )
            state.node?.let { node ->
                StatusDetail(
                    label = stringResource(resource = Res.string.node_id_label),
                    value = "${node.nodeId.slice(0..<6)}...${node.nodeId.slice((node.nodeId.length - 6)..<(node.nodeId.length))}",
                    iconPainter = painterResource(Res.drawable.network_node_24px),
                    textToCopy = node.nodeId
                )

                StatusDetail(
                    label = stringResource(resource = Res.string.home_relay_label),
                    value = node.homeRelay,
                    iconPainter = painterResource(Res.drawable.cell_tower_24px),
                    textToCopy = node.homeRelay
                )

                StatusDetail(
                    label = stringResource(resource = Res.string.connections_label),
                    value = "${node.connSuccess} success, ${node.connDirect} direct",
                    iconPainter = painterResource(Res.drawable.p2p_24px),
                )

                StatusDetail(
                    label = stringResource(resource = Res.string.sent_label),
                    value = "${node.sendIpv4} v4, ${node.sendIpv6} v6, ${node.sendRelay} relay",
                    iconPainter = painterResource(Res.drawable.arrow_upward_24px),
                )

                StatusDetail(
                    label = stringResource(resource = Res.string.received_label),
                    value = "${node.recvIpv4} v4, ${node.recvIpv6} v6, ${node.recvRelay} relay",
                    iconPainter = painterResource(Res.drawable.arrow_downward_24px),
                )
            }
        }
    }
}

@Composable
private fun StatusDetail(
    label: String,
    value: String,
    iconPainter: Painter,
    textToCopy: String? = null,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Icon(
                painter = iconPainter,
                contentDescription = label,
                modifier = Modifier.padding(8.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.labelLarge)
                Text(
                    value,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            textToCopy?.let { textToCopy ->
                CopyIconButton(
                    textToCopy = textToCopy,
                    contentDescription = stringResource(resource = Res.string.copy_button_description)
                )
            }
        }
    }
}

@Composable
fun Inputs(
    viewModel: CoreViewModel = viewModel(),
) {
    var nodeIdInput by remember { mutableStateOf("") }
    var textInput by remember { mutableStateOf("") }

    Card(modifier = Modifier.padding(16.dp)) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Connect",
                style = MaterialTheme.typography.titleLarge,
            )

            OutlinedTextField(
                value = nodeIdInput,
                onValueChange = { nodeIdInput = it },
                label = { Text("Node ID") },
                modifier = Modifier
                    .fillMaxWidth()
            )

            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                label = { Text("Text") },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Button(onClick = {
                try {
                    viewModel.instance.send(nodeIdInput, textInput)
                } catch (e: CoreException) {
                    println("send error: ${e.message()}")
                }
            }) {
                Text("Connect")
            }
        }
    }
}

@Composable
fun CopyIconButton(textToCopy: String, contentDescription: String) {
    val clipboard = LocalClipboard.current

    IconButton(
        onClick = {
            runBlocking {
                val clip = toClipEntry(textToCopy)
                clipboard.setClipEntry(clip)
                // not supported in CMP
                // Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        },
    ) {
        Icon(
            painter = painterResource(Res.drawable.content_copy_24px),
            contentDescription = contentDescription
        )
    }
}

fun mockModel(): Model {
    return Model(
        updateCount = 0u,
        node = NodeModel(
            nodeId = "5ff54e4335a0f10b7f905551fa3b79fe6d689d8622c419524aeefccad2fddf2b",
            homeRelay = "https://aps1-1.relay.iroh.network./",
            sendIpv4 = 123u,
            sendIpv6 = 123u,
            sendRelay = 123u,
            recvIpv4 = 123u,
            recvIpv6 = 123u,
            recvRelay = 123u,
            connSuccess = 123u,
            connDirect = 123u,
        )
    )
}
