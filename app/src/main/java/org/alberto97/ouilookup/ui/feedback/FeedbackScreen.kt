package org.alberto97.ouilookup.ui.feedback

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.ui.theme.OUILookupTheme
import org.alberto97.ouilookup.ui.theme.Pink400

@Composable
fun FeedbackScreen(
    viewModel: FeedbackViewModel,
    navController: NavController
) {
    FeedbackScreen(
        dismiss = { navController.popBackStack() },
        doNotShowAgain = {
            viewModel.doNotShowAgain()
            navController.popBackStack()
        },
        openAppStoreForReview = {
            viewModel.openAppStoreForReview()
            navController.popBackStack()
        }
    )
}

@Composable
fun FeedbackScreen(
    dismiss: () -> Unit,
    doNotShowAgain: () -> Unit,
    openAppStoreForReview: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.Favorite,
                    null,
                    tint = Pink400,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    stringResource(R.string.feedback_dialog_title),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Text(
                stringResource(R.string.feedback_dialog_text),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
            )
            Row {
                TextButton(onClick = doNotShowAgain) {
                    Text(stringResource(R.string.feedback_dialog_option_never).uppercase())
                }
                Spacer(modifier = Modifier.weight(1.0f))
                TextButton(onClick = dismiss) {
                    Text(stringResource(R.string.feedback_dialog_option_later).uppercase())
                }
                TextButton(onClick = openAppStoreForReview) {
                    Text(stringResource(R.string.feedback_dialog_option_yes).uppercase())
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    OUILookupTheme {
        FeedbackScreen({}, {}, {})
    }
}
