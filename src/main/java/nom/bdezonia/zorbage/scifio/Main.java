package nom.bdezonia.zorbage.scifio;

public class Main {

	public static void main(String[] args)
	{
		try {
			String filename = "/home/bdz/images/imagej-samples/lena-std.tif";
//			String filename = "/home/bdz/images/imagej-samples/t1-head.tif";
//			String filename = "/home/bdz/images/imagej-samples/clown.jpg";
//			String filename = "/home/bdz/images/modis/modis.hdf";
			
			// it looks like imagej just supports a few pixel types
			//   8 bit unsigned, 8 bit indexed color, 16 bit unsigned int, 16 bit signed int,
			//   32 bit float, 32 bit RGB color
			
			// should I change zorbage argb and rgb types to be IntCoders?
			
			// should I translate rgbs as RBG or 3 channels of byte? should it be an option?
			
			// I need to catch exceptions correctly: I loaded an hdf image and got a bomb out
			
			// is it really possible to run zorbage-netcdf, zorbage-gdal, and zorbage-bioformats
			//   or will their dependencies crash into each other. should I write separate
			//   plugins that load into a zorbage ui and the zorbage ui does not have their
			//   dependencies?

			// I read the licensing and it is very unfavorable to developers who want to use BF.
			// I think I need to use Scifio. It has a license compatible with zorbage.


			DataBundle bundle = Scifio.open(filename);
			
			System.out.println(bundle.uint8s.size());
			
		} catch (Exception e) {
			;
		}
	}
}
