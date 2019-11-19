package grondag.fonthack.font;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.math.MathHelper;

public class FontTextureHelper {

	public static int size = 512;
	public static int cellHeight  = 64;
	public static int padding = 4;
	public static int lod = 3;

	private static final float[] LUMINANCE_TO_LINEAR_MAP = new float[256];

	static  {
		for(int i = 0; i < LUMINANCE_TO_LINEAR_MAP.length; ++i) {
			LUMINANCE_TO_LINEAR_MAP[i] = (float) Math.pow(i / 255.0, 2.2D);
		}
	}

	public static void generateMipmaps(final NativeImage[] images) {
		final int limit = images.length;
		NativeImage base = images[0];

		if (limit > 0) {

			for(int i = 1; i < limit; ++i) {
				final NativeImage img = images[i];
				final NativeImageExt imgExt = (NativeImageExt)(Object) img;
				final int w = img.getWidth();
				final int h = img.getHeight();

				for(int u = 0; u < w; ++u) {
					for(int v = 0; v < h; ++v) {
						imgExt.ext_setLuminance(u, v, blendPixels(
							base.getPixelOpacity(u * 2 + 0, v * 2 + 0),
							base.getPixelOpacity(u * 2 + 1, v * 2 + 0),
							base.getPixelOpacity(u * 2 + 0, v * 2 + 1),
							base.getPixelOpacity(u * 2 + 1, v * 2 + 1)));
					}
				}

				base  = img;
			}
		}
	}

	public static int ceil16(int val) {
		return (val & 0xF) == 0 ? val : val + (~(val & 0xF) & 0xF);
	}

	private static final byte blendPixels(final int int_1, final int int_2, final int int_3, final int int_4) {
		final float v = LUMINANCE_TO_LINEAR_MAP[int_1 & 255] * 0.25f
			+ LUMINANCE_TO_LINEAR_MAP[int_2 & 255] * 0.25f
			+ LUMINANCE_TO_LINEAR_MAP[int_3 & 255] * 0.25f
			+ LUMINANCE_TO_LINEAR_MAP[int_4 & 255]* 0.25f;

		final int result = MathHelper.clamp((int)(Math.pow(v, 0.45454545454545453D) * 255.0D), 0, 255);

		return (byte) result; // < 96 ? 0 : (byte) result;
	}
}