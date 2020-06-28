/*
 * zorbage-scifio: code for loading data files using the scifio engine into zorbage data structures for further processing
 *
 * Copyright (C) 2020 Barry DeZonia
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package nom.bdezonia.zorbage.scifio;

/**
 * 
 * @author Barry DeZonia
 *
 */
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
