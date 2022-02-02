package org.alberto97.ouilookup.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.ui.theme.OUILookupTheme


@Suppress("unused")
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

@Composable
fun Searchbar(
    text: String,
    onTextChange: (value: String) -> Unit,
    searchbarTopPadding: Dp
) {
    Surface(
        color = MaterialTheme.colors.primarySurface,
        elevation = AppBarDefaults.TopAppBarElevation,
        modifier = Modifier.padding(top = searchbarTopPadding)
    ) {
        MaterialCustomTextField(
            value = text,
            onValueChange = onTextChange,
            placeholderText = stringResource(R.string.search_text_field_placeholder),
            leadingIcon = Icons.Outlined.Search,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.contentColorFor(MaterialTheme.colors.surface),
                backgroundColor = MaterialTheme.colors.surface,
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun MaterialCustomTextField(
    value: String,
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholderText: String = "Placeholder",
    leadingIcon: ImageVector? = null,
    colors: TextFieldColors  = TextFieldDefaults.textFieldColors(),
    shape: Shape = MaterialTheme.shapes.small
) {
    CustomTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        placeholderText = placeholderText,
        leadingIcon = leadingIcon,
        backgroundColor = colors.backgroundColor(enabled).value,
        placeholderColor = colors.placeholderColor(enabled).value,
        leadingIconColor = colors.leadingIconColor(enabled, false).value,
        placeholderTextStyle = MaterialTheme.typography.subtitle1,
        cursorColor = MaterialTheme.colors.primary,
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colors.onSurface
        ),
        shape = shape
    )
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (value: String) -> Unit,
    backgroundColor: Color,
    placeholderColor: Color,
    cursorColor: Color,
    leadingIconColor: Color,
    shape: Shape,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholderText: String = "Placeholder",
    textStyle: TextStyle = LocalTextStyle.current,
    placeholderTextStyle: TextStyle = LocalTextStyle.current,
    leadingIcon: ImageVector? = null,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        modifier = modifier
            .shadow(16.dp)
            .background(backgroundColor, shape)
            .fillMaxWidth(),
        singleLine = true,
        cursorBrush = SolidColor(cursorColor),
        textStyle = textStyle,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    Icon(leadingIcon, null, tint = leadingIconColor)
                }
                Box(Modifier.padding(4.dp))
                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) Text(
                        text = placeholderText,
                        style = placeholderTextStyle.copy(color = placeholderColor)
                    )
                    innerTextField()
                }
            }
        }
    )
}

@Composable
@Preview
private fun Preview() {
    OUILookupTheme {
        Searchbar(text = "", onTextChange = {}, searchbarTopPadding = 0.dp)
    }
}

@Composable
@Preview("Placeholder")
private fun PreviewPlaceholder() {
    OUILookupTheme {
        Searchbar(text = "Placeholder", onTextChange = {}, searchbarTopPadding = 0.dp)
    }
}

