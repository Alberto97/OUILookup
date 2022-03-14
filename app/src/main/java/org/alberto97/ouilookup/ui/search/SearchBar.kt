package org.alberto97.ouilookup.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
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
            leadingIcon = { Icon(Icons.Rounded.Search, null) },
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
    modifier: Modifier = Modifier,
    onTrailingIconClick: (() -> Unit)? = null,
) {
    val trailingIcon = if (text.isNotEmpty()) Icons.Rounded.Clear else null
    Surface(
        color = MaterialTheme.colors.primarySurface,
        elevation = AppBarDefaults.TopAppBarElevation,
        modifier = modifier
    ) {
        MaterialCustomTextField(
            value = text,
            onValueChange = onTextChange,
            placeholderText = stringResource(R.string.search_text_field_placeholder),
            leadingIcon = Icons.Rounded.Search,
            trailingIcon = trailingIcon,
            onTrailingIconClick = onTrailingIconClick,
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
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    shape: Shape = MaterialTheme.shapes.small
) {
    CustomTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        placeholderText = placeholderText,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onTrailingIconClick = onTrailingIconClick,
        backgroundColor = colors.backgroundColor(enabled).value,
        placeholderColor = colors.placeholderColor(enabled).value,
        leadingIconColor = colors.leadingIconColor(enabled, false).value,
        trailingIconColor = colors.trailingIconColor(enabled, false).value,
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
    trailingIconColor: Color,
    shape: Shape,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholderText: String = "Placeholder",
    textStyle: TextStyle = LocalTextStyle.current,
    placeholderTextStyle: TextStyle = LocalTextStyle.current,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
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
        textStyle = textStyle
    ) { innerTextField ->
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
            if (trailingIcon != null) {
                TrailingBox(
                    trailingIcon,
                    trailingIconColor = trailingIconColor,
                    onTrailingIconClick = onTrailingIconClick,
                )
            }
        }
    }
}

@Composable
private fun TrailingBox(
    trailingIcon: ImageVector,
    trailingIconColor: Color,
    onTrailingIconClick: (() -> Unit)? = null
) {
    if (onTrailingIconClick != null) {
        IconButton(
            onClick = onTrailingIconClick,
            modifier = Modifier.size(24.dp),
            content = {
                TrailingIcon(
                    trailingIcon = trailingIcon,
                    trailingIconColor = trailingIconColor
                )
            }
        )
    } else {
        TrailingIcon(
            trailingIcon = trailingIcon,
            trailingIconColor = trailingIconColor
        )
    }
}

@Composable
private fun TrailingIcon(
    trailingIcon: ImageVector,
    trailingIconColor: Color,
) {
    Icon(
        trailingIcon,
        null,
        tint = trailingIconColor,
    )
}

@Composable
@Preview
private fun Preview() {
    OUILookupTheme {
        Searchbar(text = "", onTextChange = {})
    }
}

@Composable
@Preview("Placeholder")
private fun PreviewPlaceholder() {
    OUILookupTheme {
        Searchbar(text = "00:AA:BB", onTextChange = {})
    }
}

