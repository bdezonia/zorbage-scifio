/*
 * zorbage-scifio: code for using the SCIFIO data file library to open files into zorbage data structures for further processing
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

	/* public */ static void main(String[] args)
	{
		String filename = "/home/bdz/images/fitsfile.fits";
//		String filename = "/home/bdz/images/imagej-samples/lena-std.tif";
//		String filename = "/home/bdz/images/imagej-samples/t1-head.tif";
//		String filename = "/home/bdz/images/imagej-samples/clown.jpg";
//		String filename = "/home/bdz/images/modis/modis.hdf";

		DataBundle bundle = Scifio.loadAllDatasets(filename);
		
		System.out.println(bundle.floats.size());
		System.out.println(bundle.floats.get(0).numDimensions());
		System.out.println(bundle.floats.get(0).dimension(0));
		System.out.println(bundle.floats.get(0).dimension(1));
		//System.out.println(bundle.floats.get(0).dimension(2));
		System.out.println("boogah");
	}
}
