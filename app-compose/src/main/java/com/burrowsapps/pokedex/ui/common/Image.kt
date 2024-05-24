package com.burrowsapps.pokedex.ui.common

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.LocalGlideRequestBuilder
import kotlin.math.roundToInt

@Composable
fun PokemonGlideImage(
  imageUrl: String,
  modifier: Modifier = Modifier,
  size: Int = 85,
) {
  val context = LocalContext.current
  val overrideSize = size.dp.value.roundToInt() // Define the size for the image and thumbnail

  val requestBuilder =
    buildRequest(
      context = context,
      imageUrl = imageUrl,
      thumbnailUrl = imageUrl,
      override = overrideSize,
    )

  // Provide the Glide request builder for GlideImage and display the image
  CompositionLocalProvider(LocalGlideRequestBuilder provides requestBuilder) {
    GlideImage(
      imageModel = { imageUrl },
      modifier =
        modifier
          .size(overrideSize.dp)
          .padding(end = 16.dp),
      loading = {
        Box(modifier = Modifier.matchParentSize()) {
          CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
          )
        }
      },
      // Consider adding an error composable if needed
    )
  }
}

private fun buildRequest(
  context: Context,
  imageUrl: String,
  thumbnailUrl: String,
  override: Int = Target.SIZE_ORIGINAL,
): RequestBuilder<Drawable> {
  val request = Glide.with(context).asDrawable()
  return request.transition(DrawableTransitionOptions.withCrossFade()).thumbnail(
    request.transition(DrawableTransitionOptions.withCrossFade()).load(thumbnailUrl)
      .override(override).signature(ObjectKey(thumbnailUrl)),
  ).override(override).signature(ObjectKey(imageUrl))
}
