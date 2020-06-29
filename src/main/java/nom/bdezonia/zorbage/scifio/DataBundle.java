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

import java.util.ArrayList;
import java.util.List;

import nom.bdezonia.zorbage.data.DimensionedDataSource;
import nom.bdezonia.zorbage.type.float32.complex.ComplexFloat32Member;
import nom.bdezonia.zorbage.type.float32.real.Float32Member;
import nom.bdezonia.zorbage.type.float64.complex.ComplexFloat64Member;
import nom.bdezonia.zorbage.type.float64.real.Float64Member;
import nom.bdezonia.zorbage.type.int1.UnsignedInt1Member;
import nom.bdezonia.zorbage.type.int10.UnsignedInt10Member;
import nom.bdezonia.zorbage.type.int11.UnsignedInt11Member;
import nom.bdezonia.zorbage.type.int12.UnsignedInt12Member;
import nom.bdezonia.zorbage.type.int128.UnsignedInt128Member;
import nom.bdezonia.zorbage.type.int13.UnsignedInt13Member;
import nom.bdezonia.zorbage.type.int14.UnsignedInt14Member;
import nom.bdezonia.zorbage.type.int15.UnsignedInt15Member;
import nom.bdezonia.zorbage.type.int16.SignedInt16Member;
import nom.bdezonia.zorbage.type.int16.UnsignedInt16Member;
import nom.bdezonia.zorbage.type.int2.UnsignedInt2Member;
import nom.bdezonia.zorbage.type.int3.UnsignedInt3Member;
import nom.bdezonia.zorbage.type.int32.SignedInt32Member;
import nom.bdezonia.zorbage.type.int32.UnsignedInt32Member;
import nom.bdezonia.zorbage.type.int4.UnsignedInt4Member;
import nom.bdezonia.zorbage.type.int5.UnsignedInt5Member;
import nom.bdezonia.zorbage.type.int6.UnsignedInt6Member;
import nom.bdezonia.zorbage.type.int64.SignedInt64Member;
import nom.bdezonia.zorbage.type.int64.UnsignedInt64Member;
import nom.bdezonia.zorbage.type.int7.UnsignedInt7Member;
import nom.bdezonia.zorbage.type.int8.SignedInt8Member;
import nom.bdezonia.zorbage.type.int8.UnsignedInt8Member;
import nom.bdezonia.zorbage.type.int9.UnsignedInt9Member;
import nom.bdezonia.zorbage.type.rgb.ArgbMember;
import nom.bdezonia.zorbage.type.unbounded.UnboundedIntMember;

/**
 * @author Barry DeZonia
 */
public class DataBundle {
	public List<DimensionedDataSource<UnsignedInt1Member>> uint1s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt2Member>> uint2s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt3Member>> uint3s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt4Member>> uint4s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt5Member>> uint5s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt6Member>> uint6s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt7Member>> uint7s = new ArrayList<>();
	public List<DimensionedDataSource<SignedInt8Member>> int8s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt8Member>> uint8s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt9Member>> uint9s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt10Member>> uint10s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt11Member>> uint11s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt12Member>> uint12s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt13Member>> uint13s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt14Member>> uint14s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt15Member>> uint15s = new ArrayList<>();
	public List<DimensionedDataSource<SignedInt16Member>> int16s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt16Member>> uint16s = new ArrayList<>();
	public List<DimensionedDataSource<SignedInt32Member>> int32s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt32Member>> uint32s = new ArrayList<>();
	public List<DimensionedDataSource<SignedInt64Member>> int64s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt64Member>> uint64s = new ArrayList<>();
	public List<DimensionedDataSource<UnsignedInt128Member>> uint128s = new ArrayList<>();
	public List<DimensionedDataSource<UnboundedIntMember>> bigs = new ArrayList<>();
	public List<DimensionedDataSource<Float32Member>> floats = new ArrayList<>();
	public List<DimensionedDataSource<Float64Member>> doubles = new ArrayList<>();
	public List<DimensionedDataSource<ComplexFloat32Member>> cfloats = new ArrayList<>();
	public List<DimensionedDataSource<ComplexFloat64Member>> cdoubles = new ArrayList<>();
	public List<DimensionedDataSource<ArgbMember>> argbs = new ArrayList<>();

	
	public void mergeUInt1(DimensionedDataSource<UnsignedInt1Member> ds) {
		uint1s.add(ds);
	}
	
	public void mergeUInt2(DimensionedDataSource<UnsignedInt2Member> ds) {
		uint2s.add(ds);
	}
	
	public void mergeUInt3(DimensionedDataSource<UnsignedInt3Member> ds) {
		uint3s.add(ds);
	}
	
	public void mergeUInt4(DimensionedDataSource<UnsignedInt4Member> ds) {
		uint4s.add(ds);
	}
	
	public void mergeUInt5(DimensionedDataSource<UnsignedInt5Member> ds) {
		uint5s.add(ds);
	}
	
	public void mergeUInt6(DimensionedDataSource<UnsignedInt6Member> ds) {
		uint6s.add(ds);
	}
	
	public void mergeUInt7(DimensionedDataSource<UnsignedInt7Member> ds) {
		uint7s.add(ds);
	}
	
	public void mergeUInt8(DimensionedDataSource<UnsignedInt8Member> ds) {
		uint8s.add(ds);
	}
	
	public void mergeInt8(DimensionedDataSource<SignedInt8Member> ds) {
		int8s.add(ds);
	}
	
	public void mergeUInt9(DimensionedDataSource<UnsignedInt9Member> ds) {
		uint9s.add(ds);
	}
	
	public void mergeUInt10(DimensionedDataSource<UnsignedInt10Member> ds) {
		uint10s.add(ds);
	}
	
	public void mergeUInt11(DimensionedDataSource<UnsignedInt11Member> ds) {
		uint11s.add(ds);
	}
	
	public void mergeUInt12(DimensionedDataSource<UnsignedInt12Member> ds) {
		uint12s.add(ds);
	}

	public void mergeUInt13(DimensionedDataSource<UnsignedInt13Member> ds) {
		uint13s.add(ds);
	}
	
	public void mergeUInt14(DimensionedDataSource<UnsignedInt14Member> ds) {
		uint14s.add(ds);
	}
	
	public void mergeUInt15(DimensionedDataSource<UnsignedInt15Member> ds) {
		uint15s.add(ds);
	}
	
	public void mergeUInt16(DimensionedDataSource<UnsignedInt16Member> ds) {
		uint16s.add(ds);
	}
	
	public void mergeInt16(DimensionedDataSource<SignedInt16Member> ds) {
		int16s.add(ds);
	}
	
	public void mergeUInt32(DimensionedDataSource<UnsignedInt32Member> ds) {
		uint32s.add(ds);
	}
	
	public void mergeInt32(DimensionedDataSource<SignedInt32Member> ds) {
		int32s.add(ds);
	}
	
	public void mergeUInt64(DimensionedDataSource<UnsignedInt64Member> ds) {
		uint64s.add(ds);
	}
	
	public void mergeInt64(DimensionedDataSource<SignedInt64Member> ds) {
		int64s.add(ds);
	}
	
	public void mergeUInt128(DimensionedDataSource<UnsignedInt128Member> ds) {
		uint128s.add(ds);
	}
	
	public void mergeBigInt(DimensionedDataSource<UnboundedIntMember> ds) {
		bigs.add(ds);
	}

	public void mergeFlt(DimensionedDataSource<Float32Member> ds) {
		floats.add(ds);
	}
	
	public void mergeDbl(DimensionedDataSource<Float64Member> ds) {
		doubles.add(ds);
	}

	public void mergeCFlt(DimensionedDataSource<ComplexFloat32Member> ds) {
		cfloats.add(ds);
	}
	
	public void mergeCDbl(DimensionedDataSource<ComplexFloat64Member> ds) {
		cdoubles.add(ds);
	}
	
	public void mergeArgb(DimensionedDataSource<ArgbMember> ds) {
		argbs.add(ds);
	}
}
