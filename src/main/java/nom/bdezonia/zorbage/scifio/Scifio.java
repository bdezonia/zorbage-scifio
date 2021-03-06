/*
 * zorbage-scifio: code for using the SCIFIO data file library to open files into zorbage data structures for further processing
 *
 * Copyright (C) 2020-2021 Barry DeZonia
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

import java.math.BigDecimal;
import java.util.List;

import io.scif.img.ImgOpener;
import io.scif.img.SCIFIOImgPlus;
import net.imglib2.RandomAccess;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.complex.ComplexDoubleType;
import net.imglib2.type.numeric.complex.ComplexFloatType;
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
import net.imglib2.type.numeric.integer.UnsignedVariableBitLengthType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import nom.bdezonia.zorbage.algebra.Allocatable;
import nom.bdezonia.zorbage.algorithm.GridIterator;
import nom.bdezonia.zorbage.coordinates.LinearNdCoordinateSpace;
import nom.bdezonia.zorbage.data.DimensionedDataSource;
import nom.bdezonia.zorbage.data.DimensionedStorage;
import nom.bdezonia.zorbage.misc.DataBundle;
import nom.bdezonia.zorbage.procedure.Procedure2;
import nom.bdezonia.zorbage.sampling.IntegerIndex;
import nom.bdezonia.zorbage.sampling.SamplingIterator;
import nom.bdezonia.zorbage.type.color.ArgbMember;
import nom.bdezonia.zorbage.type.complex.float32.ComplexFloat32Member;
import nom.bdezonia.zorbage.type.complex.float64.ComplexFloat64Member;
import nom.bdezonia.zorbage.type.integer.int1.UnsignedInt1Member;
import nom.bdezonia.zorbage.type.integer.int10.UnsignedInt10Member;
import nom.bdezonia.zorbage.type.integer.int11.UnsignedInt11Member;
import nom.bdezonia.zorbage.type.integer.int12.UnsignedInt12Member;
import nom.bdezonia.zorbage.type.integer.int128.UnsignedInt128Member;
import nom.bdezonia.zorbage.type.integer.int13.UnsignedInt13Member;
import nom.bdezonia.zorbage.type.integer.int14.UnsignedInt14Member;
import nom.bdezonia.zorbage.type.integer.int15.UnsignedInt15Member;
import nom.bdezonia.zorbage.type.integer.int16.SignedInt16Member;
import nom.bdezonia.zorbage.type.integer.int16.UnsignedInt16Member;
import nom.bdezonia.zorbage.type.integer.int2.UnsignedInt2Member;
import nom.bdezonia.zorbage.type.integer.int3.UnsignedInt3Member;
import nom.bdezonia.zorbage.type.integer.int32.SignedInt32Member;
import nom.bdezonia.zorbage.type.integer.int32.UnsignedInt32Member;
import nom.bdezonia.zorbage.type.integer.int4.UnsignedInt4Member;
import nom.bdezonia.zorbage.type.integer.int5.UnsignedInt5Member;
import nom.bdezonia.zorbage.type.integer.int6.UnsignedInt6Member;
import nom.bdezonia.zorbage.type.integer.int64.SignedInt64Member;
import nom.bdezonia.zorbage.type.integer.int64.UnsignedInt64Member;
import nom.bdezonia.zorbage.type.integer.int7.UnsignedInt7Member;
import nom.bdezonia.zorbage.type.integer.int8.SignedInt8Member;
import nom.bdezonia.zorbage.type.integer.int8.UnsignedInt8Member;
import nom.bdezonia.zorbage.type.integer.int9.UnsignedInt9Member;
import nom.bdezonia.zorbage.type.integer.unbounded.UnboundedIntMember;
import nom.bdezonia.zorbage.type.real.float32.Float32Member;
import nom.bdezonia.zorbage.type.real.float64.Float64Member;

/**
 * 
 * @author Barry DeZonia
 *
 */
public class Scifio {

	/**
	 * 
	 * @param filename
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static DataBundle loadAllDatasets(String filename) {
		
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
				bundle.mergeFlt32( loadFloatImage( (SCIFIOImgPlus<FloatType>) scifImgPlus) );
			else if (elem instanceof DoubleType)
				bundle.mergeFlt64( loadDoubleImage( (SCIFIOImgPlus<DoubleType>) scifImgPlus) );
			else if (elem instanceof Unsigned2BitType)
				bundle.mergeUInt2( loadUnsigned2BitImage( (SCIFIOImgPlus<Unsigned2BitType>) scifImgPlus) );
			else if (elem instanceof Unsigned4BitType)
				bundle.mergeUInt4( loadUnsigned4BitImage( (SCIFIOImgPlus<Unsigned4BitType>) scifImgPlus) );
			else if (elem instanceof Unsigned12BitType)
				bundle.mergeUInt12( loadUnsigned12BitImage( (SCIFIOImgPlus<Unsigned12BitType>) scifImgPlus) );
			else if (elem instanceof Unsigned128BitType)
				bundle.mergeUInt128( loadUnsigned128BitImage( (SCIFIOImgPlus<Unsigned128BitType>) scifImgPlus) );
			else if (elem instanceof ComplexFloatType)
				bundle.mergeComplexFlt32( loadComplexFloatImage( (SCIFIOImgPlus<ComplexFloatType>) scifImgPlus) );
			else if (elem instanceof ComplexDoubleType)
				bundle.mergeComplexFlt64( loadComplexDoubleImage( (SCIFIOImgPlus<ComplexDoubleType>) scifImgPlus) );
			else if (elem instanceof ARGBType)
				bundle.mergeArgb( loadARGBTypeImage( (SCIFIOImgPlus<ARGBType>) scifImgPlus) );
			else if (elem instanceof UnsignedVariableBitLengthType) {
				UnsignedVariableBitLengthType type = (UnsignedVariableBitLengthType) elem;
				int bpp = type.getBitsPerPixel();
				if (bpp < 1)
					throw new IllegalArgumentException("bit per pix must be > 0");
				switch (bpp) {
				case 1:
					bundle.mergeUInt1( loadUnsignedV1BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				case 2:
					bundle.mergeUInt2( loadUnsignedV2BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				case 3:
					bundle.mergeUInt3( loadUnsignedV3BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				case 4:
					bundle.mergeUInt4( loadUnsignedV4BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				case 5:
					bundle.mergeUInt5( loadUnsignedV5BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				case 6:
					bundle.mergeUInt6( loadUnsignedV6BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				case 7:
					bundle.mergeUInt7( loadUnsignedV7BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				case 8:
					bundle.mergeUInt8( loadUnsignedV8BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				case 9:
					bundle.mergeUInt9( loadUnsignedV9BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				case 10:
					bundle.mergeUInt10( loadUnsignedV10BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				case 11:
					bundle.mergeUInt11( loadUnsignedV11BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				case 12:
					bundle.mergeUInt12( loadUnsignedV12BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				case 13:
					bundle.mergeUInt13( loadUnsignedV13BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				case 14:
					bundle.mergeUInt14( loadUnsignedV14BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				case 15:
					bundle.mergeUInt15( loadUnsignedV15BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				case 16:
					bundle.mergeUInt16( loadUnsignedV16BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				default:
					if (bpp <= 32)
						bundle.mergeUInt32( loadUnsignedV32BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					else if (bpp <= 64)
						bundle.mergeUInt64( loadUnsignedV64BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					else if (bpp <= 128)
						bundle.mergeUInt128( loadUnsignedV128BitImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					else // bpp > 128
						bundle.mergeBigInt( loadUnsignedBigIntImage( (SCIFIOImgPlus<UnsignedVariableBitLengthType>) scifImgPlus) );
					break;
				}
			}
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
		DimensionedDataSource<UnsignedInt8Member> output =
				makeDataset(input, new UnsignedInt8Member());
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
		DimensionedDataSource<SignedInt8Member> output =
				makeDataset(input, new SignedInt8Member());
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
		DimensionedDataSource<UnsignedInt16Member> output =
				makeDataset(input, new UnsignedInt16Member());
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
		DimensionedDataSource<SignedInt16Member> output =
				makeDataset(input, new SignedInt16Member());
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
		DimensionedDataSource<UnsignedInt32Member> output =
				makeDataset(input, new UnsignedInt32Member());
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
		DimensionedDataSource<SignedInt32Member> output =
				makeDataset(input, new SignedInt32Member());
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
		DimensionedDataSource<UnsignedInt64Member> output =
				makeDataset(input, new UnsignedInt64Member());
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
		DimensionedDataSource<SignedInt64Member> output =
				makeDataset(input, new SignedInt64Member());
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
		DimensionedDataSource<Float32Member> output =
				makeDataset(input, new Float32Member());
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
		DimensionedDataSource<Float64Member> output =
				makeDataset(input, new Float64Member());
		fillDataset(input, proc, new Float64Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<ComplexFloat32Member>
		loadComplexFloatImage(SCIFIOImgPlus<ComplexFloatType> input)
	{
		Procedure2<ComplexFloatType, ComplexFloat32Member> proc =
				new Procedure2<ComplexFloatType, ComplexFloat32Member>()
		{
			@Override
			public void call(ComplexFloatType in, ComplexFloat32Member out) {
				out.setR(in.getRealFloat());
				out.setI(in.getImaginaryFloat());
			}
		};
		DimensionedDataSource<ComplexFloat32Member> output =
				makeDataset(input, new ComplexFloat32Member());
		fillDataset(input, proc, new ComplexFloat32Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<ComplexFloat64Member>
		loadComplexDoubleImage(SCIFIOImgPlus<ComplexDoubleType> input)
	{
		Procedure2<ComplexDoubleType, ComplexFloat64Member> proc =
				new Procedure2<ComplexDoubleType, ComplexFloat64Member>()
		{
			@Override
			public void call(ComplexDoubleType in, ComplexFloat64Member out) {
				out.setR(in.getRealDouble());
				out.setI(in.getImaginaryDouble());
			}
		};
		DimensionedDataSource<ComplexFloat64Member> output =
				makeDataset(input, new ComplexFloat64Member());
		fillDataset(input, proc, new ComplexFloat64Member(), output);
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
		DimensionedDataSource<UnsignedInt2Member> output =
				makeDataset(input, new UnsignedInt2Member());
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
		DimensionedDataSource<UnsignedInt4Member> output =
				makeDataset(input, new UnsignedInt4Member());
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
		DimensionedDataSource<UnsignedInt12Member> output =
				makeDataset(input, new UnsignedInt12Member());
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
		DimensionedDataSource<UnsignedInt128Member> output =
				makeDataset(input, new UnsignedInt128Member());
		fillDataset(input, proc, new UnsignedInt128Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt1Member>
		loadUnsignedV1BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt1Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt1Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt1Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt1Member> output =
				makeDataset(input, new UnsignedInt1Member());
		fillDataset(input, proc, new UnsignedInt1Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt2Member>
		loadUnsignedV2BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt2Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt2Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt2Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt2Member> output =
				makeDataset(input, new UnsignedInt2Member());
		fillDataset(input, proc, new UnsignedInt2Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt3Member>
		loadUnsignedV3BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt3Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt3Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt3Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt3Member> output =
				makeDataset(input, new UnsignedInt3Member());
		fillDataset(input, proc, new UnsignedInt3Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt4Member>
		loadUnsignedV4BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt4Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt4Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt4Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt4Member> output =
				makeDataset(input, new UnsignedInt4Member());
		fillDataset(input, proc, new UnsignedInt4Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt5Member>
		loadUnsignedV5BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt5Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt5Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt5Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt5Member> output =
				makeDataset(input, new UnsignedInt5Member());
		fillDataset(input, proc, new UnsignedInt5Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt6Member>
		loadUnsignedV6BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt6Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt6Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt6Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt6Member> output =
				makeDataset(input, new UnsignedInt6Member());
		fillDataset(input, proc, new UnsignedInt6Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt7Member>
		loadUnsignedV7BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt7Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt7Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt7Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt7Member> output =
				makeDataset(input, new UnsignedInt7Member());
		fillDataset(input, proc, new UnsignedInt7Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt8Member>
		loadUnsignedV8BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt8Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt8Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt8Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt8Member> output =
				makeDataset(input, new UnsignedInt8Member());
		fillDataset(input, proc, new UnsignedInt8Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt9Member>
		loadUnsignedV9BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt9Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt9Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt9Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt9Member> output =
				makeDataset(input, new UnsignedInt9Member());
		fillDataset(input, proc, new UnsignedInt9Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt10Member>
		loadUnsignedV10BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt10Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt10Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt10Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt10Member> output =
				makeDataset(input, new UnsignedInt10Member());
		fillDataset(input, proc, new UnsignedInt10Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt11Member>
		loadUnsignedV11BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt11Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt11Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt11Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt11Member> output =
				makeDataset(input, new UnsignedInt11Member());
		fillDataset(input, proc, new UnsignedInt11Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt12Member>
		loadUnsignedV12BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt12Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt12Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt12Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt12Member> output =
				makeDataset(input, new UnsignedInt12Member());
		fillDataset(input, proc, new UnsignedInt12Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt13Member>
		loadUnsignedV13BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt13Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt13Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt13Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt13Member> output =
				makeDataset(input, new UnsignedInt13Member());
		fillDataset(input, proc, new UnsignedInt13Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt14Member>
		loadUnsignedV14BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt14Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt14Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt14Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt14Member> output =
				makeDataset(input, new UnsignedInt14Member());
		fillDataset(input, proc, new UnsignedInt14Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt15Member>
		loadUnsignedV15BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt15Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt15Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt15Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt15Member> output =
				makeDataset(input, new UnsignedInt15Member());
		fillDataset(input, proc, new UnsignedInt15Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt16Member>
		loadUnsignedV16BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt16Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt16Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt16Member out) {
				out.setV(in.getInteger());
			}
		};
		DimensionedDataSource<UnsignedInt16Member> output =
				makeDataset(input, new UnsignedInt16Member());
		fillDataset(input, proc, new UnsignedInt16Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt32Member>
		loadUnsignedV32BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt32Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt32Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt32Member out) {
				out.setV(in.get());
			}
		};
		DimensionedDataSource<UnsignedInt32Member> output =
				makeDataset(input, new UnsignedInt32Member());
		fillDataset(input, proc, new UnsignedInt32Member(), output);
		updateMetadata(input, output);
		return output;
	}

	private static DimensionedDataSource<UnsignedInt64Member>
		loadUnsignedV64BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt64Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt64Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt64Member out) {
				out.setV(in.getBigInteger());
			}
		};
		DimensionedDataSource<UnsignedInt64Member> output =
				makeDataset(input, new UnsignedInt64Member());
		fillDataset(input, proc, new UnsignedInt64Member(), output);
		updateMetadata(input, output);
		return output;
	}
	
	private static DimensionedDataSource<UnsignedInt128Member>
		loadUnsignedV128BitImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnsignedInt128Member> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnsignedInt128Member>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnsignedInt128Member out) {
				out.setV(in.getBigInteger());
			}
		};
		DimensionedDataSource<UnsignedInt128Member> output =
				makeDataset(input, new UnsignedInt128Member());
		fillDataset(input, proc, new UnsignedInt128Member(), output);
		updateMetadata(input, output);
		return output;
	}
	
	private static DimensionedDataSource<UnboundedIntMember>
		loadUnsignedBigIntImage(SCIFIOImgPlus<UnsignedVariableBitLengthType> input)
	{
		Procedure2<UnsignedVariableBitLengthType, UnboundedIntMember> proc =
				new Procedure2<UnsignedVariableBitLengthType, UnboundedIntMember>()
		{
			@Override
			public void call(UnsignedVariableBitLengthType in, UnboundedIntMember out) {
				out.setV(in.getBigInteger());
			}
		};
		DimensionedDataSource<UnboundedIntMember> output =
				makeDataset(input, new UnboundedIntMember());
		fillDataset(input, proc, new UnboundedIntMember(), output);
		updateMetadata(input, output);
		return output;
	}
	
	private static DimensionedDataSource<ArgbMember>
		loadARGBTypeImage(SCIFIOImgPlus<ARGBType> input)
	{
		Procedure2<ARGBType, ArgbMember> proc =
				new Procedure2<ARGBType, ArgbMember>()
		{
			@Override
			public void call(ARGBType in, ArgbMember out) {
				int value = in.get();
				out.setA(ARGBType.alpha(value));
				out.setR(ARGBType.red(value));
				out.setG(ARGBType.green(value));
				out.setB(ARGBType.blue(value));
			}
		};
		DimensionedDataSource<ArgbMember> output =
				makeDataset(input, new ArgbMember());
		fillDataset(input, proc, new ArgbMember(), output);
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
		return DimensionedStorage.allocate(type, dims);
	}
	
	private static <U,W>
		void fillDataset(SCIFIOImgPlus<U> input, Procedure2<U,W> converter, W outValue, DimensionedDataSource<W> output)
	{
		int numD = output.numDimensions();
		
		IntegerIndex index = new IntegerIndex(numD);
		
		SamplingIterator<IntegerIndex> iter = GridIterator.compute(output);
		
		RandomAccess<U> r = input.randomAccess();
		
		long[] pnt = new long[numD];
		
		while (iter.hasNext()) {
			iter.next(index);
			for (int i = 0; i < numD; i++) {
				pnt[i] = index.get(i);
			}
			r.setPosition(pnt);
			U inValue = r.get();
			converter.call(inValue, outValue);
			output.set(index, outValue);
		}
	}

	private static void updateMetadata(SCIFIOImgPlus<?> input, DimensionedDataSource<?> output)
	{
		BigDecimal[] scales = new BigDecimal[input.numDimensions()];
		BigDecimal[] offsets = new BigDecimal[input.numDimensions()];
		output.setName(input.getName());
		output.setSource(input.getSource());
		for (int i = 0; i < input.numDimensions(); i++) {
			output.setAxisType(i, input.axis(i).type().toString());
			output.setAxisUnit(i, input.axis(i).unit());
			if (input.dimension(i) < 2)
				scales[i] = BigDecimal.ONE;
			else
				scales[i] = BigDecimal.valueOf(input.axis(i).averageScale(0, input.dimension(i)-1));
			offsets[i] = BigDecimal.valueOf(input.axis(i).calibratedValue(0));
		}
		output.setCoordinateSpace(new LinearNdCoordinateSpace(scales, offsets));
		output.metadata().put("input-dataset-name", input.getMetadata().getDatasetName());
		output.metadata().put("input-dataset-size", Long.valueOf(input.getMetadata().getDatasetSize()).toString());
		// surprisingly this one might hang or just takes a long time with ImageJ's lena-std
		// image. maybe a scifio bug? I reported it as one.
		//output.metadata().put("input-destination-location", input.getMetadata().getDestinationLocation().toString());
		output.metadata().put("input-format-name", input.getMetadata().getFormatName());
		output.metadata().put("input-identifier", input.getMetadata().getIdentifier());
		output.metadata().put("input-location", input.getMetadata().getLocation());
		output.metadata().put("input-source-location", input.getMetadata().getSourceLocation().toString());
		output.metadata().put("input-version", input.getMetadata().getVersion());
	}
}
