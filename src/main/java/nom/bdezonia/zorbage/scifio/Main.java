/*
 * zorbage-scifio: code for using the SCIFIO data file library to open files into zorbage data structures for further processing
 *
 * Copyright (C) 2020-2022 Barry DeZonia
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package nom.bdezonia.zorbage.scifio;

import nom.bdezonia.zorbage.misc.DataBundle;

/**
 * 
 * @author Barry DeZonia
 *
 */
public class Main {

	/* public */ static void main(String[] args)
	{
		String filename = "/home/bdz/images/fitsfile.fits";
//		String filename = "/home/bdz/images/imagej-samples/lena-std.tif";
//		String filename = "/home/bdz/images/imagej-samples/t1-head.tif";
//		String filename = "/home/bdz/images/imagej-samples/clown.jpg";
//		String filename = "/home/bdz/images/modis/modis.hdf";

		DataBundle bundle = Scifio.readAllDatasets(filename);
		
		System.out.println(bundle.flts.size());
		System.out.println(bundle.flts.get(0).numDimensions());
		System.out.println(bundle.flts.get(0).dimension(0));
		System.out.println(bundle.flts.get(0).dimension(1));
		//System.out.println(bundle.floats.get(0).dimension(2));
		System.out.println("boogah");
	}
}
