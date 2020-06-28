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

import java.util.Iterator;
import java.util.List;

import io.scif.img.ImgOpener;
import io.scif.img.SCIFIOImgPlus;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.integer.Unsigned128BitType;
import net.imglib2.type.numeric.integer.Unsigned12BitType;
import net.imglib2.type.numeric.integer.Unsigned2BitType;
import net.imglib2.type.numeric.integer.Unsigned4BitType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedIntType;
import net.imglib2.type.numeric.integer.UnsignedLongType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import nom.bdezonia.zorbage.algebra.Allocatable;
import nom.bdezonia.zorbage.data.DimensionedDataSource;
import nom.bdezonia.zorbage.data.DimensionedStorage;
import nom.bdezonia.zorbage.procedure.Procedure2;
import nom.bdezonia.zorbage.type.float32.real.Float32Member;
import nom.bdezonia.zorbage.type.float64.real.Float64Member;
import nom.bdezonia.zorbage.type.int12.UnsignedInt12Member;
import nom.bdezonia.zorbage.type.int128.UnsignedInt128Member;
import nom.bdezonia.zorbage.type.int16.SignedInt16Member;
import nom.bdezonia.zorbage.type.int16.UnsignedInt16Member;
import nom.bdezonia.zorbage.type.int2.UnsignedInt2Member;
import nom.bdezonia.zorbage.type.int32.SignedInt32Member;
import nom.bdezonia.zorbage.type.int32.UnsignedInt32Member;
import nom.bdezonia.zorbage.type.int4.UnsignedInt4Member;
import nom.bdezonia.zorbage.type.int64.SignedInt64Member;
import nom.bdezonia.zorbage.type.int64.UnsignedInt64Member;
import nom.bdezonia.zorbage.type.int8.SignedInt8Member;
import nom.bdezonia.zorbage.type.int8.UnsignedInt8Member;

/**
 * 
 * @author Barry DeZonia
 *
 */
public class Scifio {

	// Not handling:
	//   1) complex float and complex double data (can scifio even return these?)
	//   2) variable bit length data (they could be read and put into nearest types
	//        I have)
	
	/**
	 * 
	 * @param filename
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static DataBundle open(String filename) {
		
		DataBundle bundle = new DataBundle();
		
		ImgOpener opener = new ImgOpener();
		
		List<SCIFIOImgPlus<?>> results = opener.openImgs(filename);

		for (SCIFIOImgPlus<?> scifImgPlus : results) {
			Object elem = scifImgPlus.firstElement();
			if (elem instanceof UnsignedByteType)
				bundle.mergeUInt8( loadUnsignedByteImage( (SCIFIOImgPlus<UnsignedByteType>) scifImgPlus) );
			else if (elem instanceof ByteType)
				bundle.mergeInt8( loadByteImage( (SCIFIOImgPlus<ByteType>) scifImgPlus) );
			else if (elem instanceof UnsignedShortType)
				bundle.mergeUInt16( loadUnsignedShortImage( (SCIFIOImgPlus<UnsignedShortType>) scifImgPlus) );
			else if (elem instanceof ShortType)
				bundle.mergeInt16( loadShortImage( (SCIFIOImgPlus<ShortType>) scifImgPlus) );
			else if (elem instanceof UnsignedIntType)
				bundle.mergeUInt32( loadUnsignedIntImage( (SCIFIOImgPlus<UnsignedIntType>) scifImgPlus) );
			else if (elem instanceof IntType)
				bundle.mergeInt32( loadIntImage( (SCIFIOImgPlus<IntType>) scifImgPlus) );
			else if (elem instanceof UnsignedLongType)
				bundle.mergeUInt64( loadUnsignedLongImage( (SCIFIOImgPlus<UnsignedLongType>) scifImgPlus) );
			else if (elem instanceof LongType)
				bundle.mergeInt64( loadLongImage( (SCIFIOImgPlus<LongType>) scifImgPlus) );
			else if (elem instanceof FloatType)
				bundle.mergeFlt( loadFloatImage( (SCIFIOImgPlus<FloatType>) scifImgPlus) );
			else if (elem instanceof DoubleType)
				bundle.mergeDbl( loadDoubleImage( (SCIFIOImgPlus<DoubleType>) scifImgPlus) );
			else if (elem instanceof Unsigned2BitType)
				bundle.mergeUInt2( loadUnsigned2BitImage( (SCIFIOImgPlus<Unsigned2BitType>) scifImgPlus) );
			else if (elem instanceof Unsigned4BitType)
				bundle.mergeUInt4( loadUnsigned4BitImage( (SCIFIOImgPlus<Unsigned4BitType>) scifImgPlus) );
			else if (elem instanceof Unsigned12BitType)
				bundle.mergeUInt12( loadUnsigned12BitImage( (SCIFIOImgPlus<Unsigned12BitType>) scifImgPlus) );
			else if (elem instanceof Unsigned128BitType)
				bundle.mergeUInt128( loadUnsigned128BitImage( (SCIFIOImgPlus<Unsigned128BitType>) scifImgPlus) );
			else
				System.out.println("scifio image is of unknown type: " + elem);
		}
		
		return bundle;
	}
	
	private static DimensionedDataSource<UnsignedInt8Member>
		loadUnsignedByteImage(SCIFIOImgPlus<UnsignedByteType> input)
	{
		Procedure2<UnsignedByteType, UnsignedInt8Member> proc =
				new Procedure2<UnsignedByteType, UnsignedInt8Member>()
		{
			@Override
			public void call(UnsignedByteType in, UnsignedInt8Member out) {
				out.setV(in.get());
			}
		};
		DimensionedDataSource<UnsignedInt8Member> output = makeDataset(input, new UnsignedInt8Member());
		fillDataset(input, proc, new UnsignedInt8Member(), output);
		updateMetadata(input, output);
		return output;
	}
	
	private static DimensionedDataSource<SignedInt8Member>
		loadByteImage(SCIFIOImgPlus<ByteType> input)
	{
		Procedure2<ByteType, SignedInt8Member> proc =
				new Procedure2<ByteType, SignedInt8Member>()
		{
			@Override
			public void call(ByteType in, SignedInt8Member out) {
				out.setV(in.get());
			}
		};
		DimensionedDataSource<SignedInt8Member> output = makeDataset(input, new SignedInt8Member());
		fillDataset(input, proc, new SignedInt8Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt16Member>
		loadUnsignedShortImage(SCIFIOImgPlus<UnsignedShortType> input)
	{
		Procedure2<UnsignedShortType, UnsignedInt16Member> proc =
				new Procedure2<UnsignedShortType, UnsignedInt16Member>()
		{
			@Override
			public void call(UnsignedShortType in, UnsignedInt16Member out) {
				out.setV(in.get());
			}
		};
		DimensionedDataSource<UnsignedInt16Member> output = makeDataset(input, new UnsignedInt16Member());
		fillDataset(input, proc, new UnsignedInt16Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<SignedInt16Member>
		loadShortImage(SCIFIOImgPlus<ShortType> input)
	{
		Procedure2<ShortType, SignedInt16Member> proc =
				new Procedure2<ShortType, SignedInt16Member>()
		{
			@Override
			public void call(ShortType in, SignedInt16Member out) {
				out.setV(in.get());
			}
		};
		DimensionedDataSource<SignedInt16Member> output = makeDataset(input, new SignedInt16Member());
		fillDataset(input, proc, new SignedInt16Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt32Member>
		loadUnsignedIntImage(SCIFIOImgPlus<UnsignedIntType> input)
	{
		Procedure2<UnsignedIntType, UnsignedInt32Member> proc =
				new Procedure2<UnsignedIntType, UnsignedInt32Member>()
		{
			@Override
			public void call(UnsignedIntType in, UnsignedInt32Member out) {
				out.setV(in.get());
			}
		};
		DimensionedDataSource<UnsignedInt32Member> output = makeDataset(input, new UnsignedInt32Member());
		fillDataset(input, proc, new UnsignedInt32Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<SignedInt32Member>
		loadIntImage(SCIFIOImgPlus<IntType> input)
	{
		Procedure2<IntType, SignedInt32Member> proc =
				new Procedure2<IntType, SignedInt32Member>()
		{
			@Override
			public void call(IntType in, SignedInt32Member out) {
				out.setV(in.get());
			}
		};
		DimensionedDataSource<SignedInt32Member> output = makeDataset(input, new SignedInt32Member());
		fillDataset(input, proc, new SignedInt32Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt64Member>
		loadUnsignedLongImage(SCIFIOImgPlus<UnsignedLongType> input)
	{
		Procedure2<UnsignedLongType, UnsignedInt64Member> proc =
				new Procedure2<UnsignedLongType, UnsignedInt64Member>()
		{
			@Override
			public void call(UnsignedLongType in, UnsignedInt64Member out) {
				out.setV(in.getBigInteger());
			}
		};
		DimensionedDataSource<UnsignedInt64Member> output = makeDataset(input, new UnsignedInt64Member());
		fillDataset(input, proc, new UnsignedInt64Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<SignedInt64Member>
		loadLongImage(SCIFIOImgPlus<LongType> input)
	{
		Procedure2<LongType, SignedInt64Member> proc =
				new Procedure2<LongType, SignedInt64Member>()
		{
			@Override
			public void call(LongType in, SignedInt64Member out) {
				out.setV(in.get());
			}
		};
		DimensionedDataSource<SignedInt64Member> output = makeDataset(input, new SignedInt64Member());
		fillDataset(input, proc, new SignedInt64Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<Float32Member>
		loadFloatImage(SCIFIOImgPlus<FloatType> input)
	{
		Procedure2<FloatType, Float32Member> proc =
				new Procedure2<FloatType, Float32Member>()
		{
			@Override
			public void call(FloatType in, Float32Member out) {
				out.setV(in.get());
			}
		};
		DimensionedDataSource<Float32Member> output = makeDataset(input, new Float32Member());
		fillDataset(input, proc, new Float32Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<Float64Member>
		loadDoubleImage(SCIFIOImgPlus<DoubleType> input)
	{
		Procedure2<DoubleType, Float64Member> proc =
				new Procedure2<DoubleType, Float64Member>()
		{
			@Override
			public void call(DoubleType in, Float64Member out) {
				out.setV(in.get());
			}
		};
		DimensionedDataSource<Float64Member> output = makeDataset(input, new Float64Member());
		fillDataset(input, proc, new Float64Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt2Member>
		loadUnsigned2BitImage(SCIFIOImgPlus<Unsigned2BitType> input)
	{
		Procedure2<Unsigned2BitType, UnsignedInt2Member> proc =
				new Procedure2<Unsigned2BitType, UnsignedInt2Member>()
		{
			@Override
			public void call(Unsigned2BitType in, UnsignedInt2Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt2Member> output = makeDataset(input, new UnsignedInt2Member());
		fillDataset(input, proc, new UnsignedInt2Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt4Member>
		loadUnsigned4BitImage(SCIFIOImgPlus<Unsigned4BitType> input)
	{
		Procedure2<Unsigned4BitType, UnsignedInt4Member> proc =
				new Procedure2<Unsigned4BitType, UnsignedInt4Member>()
		{
			@Override
			public void call(Unsigned4BitType in, UnsignedInt4Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt4Member> output = makeDataset(input, new UnsignedInt4Member());
		fillDataset(input, proc, new UnsignedInt4Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt12Member>
		loadUnsigned12BitImage(SCIFIOImgPlus<Unsigned12BitType> input)
	{
		Procedure2<Unsigned12BitType, UnsignedInt12Member> proc =
				new Procedure2<Unsigned12BitType, UnsignedInt12Member>()
		{
			@Override
			public void call(Unsigned12BitType in, UnsignedInt12Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt12Member> output = makeDataset(input, new UnsignedInt12Member());
		fillDataset(input, proc, new UnsignedInt12Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt128Member>
		loadUnsigned128BitImage(SCIFIOImgPlus<Unsigned128BitType> input)
	{
		Procedure2<Unsigned128BitType, UnsignedInt128Member> proc =
				new Procedure2<Unsigned128BitType, UnsignedInt128Member>()
		{
			@Override
			public void call(Unsigned128BitType in, UnsignedInt128Member out) {
				out.setV(in.get());
			}
		};
		DimensionedDataSource<UnsignedInt128Member> output = makeDataset(input, new UnsignedInt128Member());
		fillDataset(input, proc, new UnsignedInt128Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static <U extends Allocatable<U>> DimensionedDataSource<U>
		makeDataset(SCIFIOImgPlus<?> sciImgPlus, U type)
	{
		long[] dims = new long[sciImgPlus.numDimensions()];
		for (int i = 0; i < dims.length; i++) {
			dims[i] = sciImgPlus.dimension(i);
		}
		return DimensionedStorage.allocate(dims, type);
	}
	
	private static <U,W>
		void fillDataset(SCIFIOImgPlus<U> input, Procedure2<U,W> converter, W outValue, DimensionedDataSource<W> output)
	{
		// Note: this code is assuming that scifio/imglib and zorbage access pixels in the same
		// order. To se the safest I should use a cartesian inetegr grid sampling and use a
		// random access for Imglib and a IntegerIndex for zorbage.
		
		long i = 0;
		Iterator<U> iter = input.iterator();
		while (iter.hasNext()) {
			U inValue = iter.next();
			converter.call(inValue, outValue);
			output.rawData().set(i, outValue);
			i++;
		}
	}

	private static void updateMetadata(SCIFIOImgPlus<?> input, DimensionedDataSource<?> output)
	{
		output.setName(input.getName());
		output.setSource(input.getSource());
		for (int i = 0; i < input.numDimensions(); i++) {
			output.setAxisType(i, input.axis(i).type().toString());
			output.setAxisUnit(i, input.axis(i).unit());
		}
		output.metadata().put("input-dataset-name", input.getMetadata().getDatasetName());
		output.metadata().put("input-dataset-size", Long.valueOf(input.getMetadata().getDatasetSize()).toString());
		// surprisingly this one might hangs or just takes a long time with ImageJ's lena-std
		// image. maybe a scifio bug? I reported it as one.
		//output.metadata().put("input-destination-location", input.getMetadata().getDestinationLocation().toString());
		output.metadata().put("input-format-name", input.getMetadata().getFormatName());
		output.metadata().put("input-identifier", input.getMetadata().getIdentifier());
		output.metadata().put("input-location", input.getMetadata().getLocation());
		output.metadata().put("input-source-location", input.getMetadata().getSourceLocation().toString());
		output.metadata().put("input-version", input.getMetadata().getVersion());
	}
}
