package org.alberto97.ouilookup.ui.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.alberto97.ouilookup.R


@Composable
fun SearchBar(
    text: String,
    onTextChange: (value: String) -> Unit,
    searchbarTopPadding: Dp,
) {
    Surface(
        color = MaterialTheme.colors.primarySurface,
        elevation = AppBarDefaults.TopAppBarElevation,
        modifier = Modifier.padding(top = searchbarTopPadding)
    ) {
        TextField(
            value = text,
            placeholder = { Text(stringResource(R.string.search_text_field_placeholder)) },
            onValueChange = onTextChange,
            leadingIcon = { Icon(Icons.Outlined.Search, null) },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.contentColorFor(MaterialTheme.colors.surface),
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        )
    }
}
