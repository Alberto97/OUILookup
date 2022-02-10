package org.alberto97.ouilookup.ui.search

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.ui.FullscreenPlaceholder
import org.alberto97.ouilookup.ui.theme.OUILookupTheme

enum class UiSearchPlaceholder {
    Updating,
    Instructions,
    NoResults
}

@Composable
fun SearchPlaceholder(placeholder: UiSearchPlaceholder) {
    when(placeholder) {
        UiSearchPlaceholder.Instructions -> InstructionsPlaceholder()
        UiSearchPlaceholder.NoResults -> NoResultsPlaceholder()
        UiSearchPlaceholder.Updating -> UpdatingPlaceholder()
    }
}

@Composable
private fun InstructionsPlaceholder() {
    FullscreenPlaceholder(
        text = stringResource(R.string.search_placeholder_instructions),
        icon = Icons.Outlined.Search
    )
}

@Composable
private fun NoResultsPlaceholder() {
    FullscreenPlaceholder(
        text = stringResource(R.string.search_placeholder_no_results),
        icon = Icons.Outlined.SearchOff
    )
}

@Composable
private fun UpdatingPlaceholder() {
    FullscreenPlaceholder(
        text = stringResource(R.string.search_placeholder_updating),
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(62.dp)
                .padding(8.dp)
        )
    }
}

@Preview("Instructions")
@Composable
private fun PreviewInstructions() {
    OUILookupTheme {
        Surface {
            SearchPlaceholder(UiSearchPlaceholder.Instructions)
        }
    }
}

@Preview("No Results")
@Composable
private fun PreviewNoResults() {
    OUILookupTheme {
        Surface {
            SearchPlaceholder(UiSearchPlaceholder.NoResults)
        }
    }
}

@Preview("Updating")
@Composable
private fun PreviewUpdating() {
    OUILookupTheme {
        Surface {
            SearchPlaceholder(UiSearchPlaceholder.Updating)
        }
    }
}
