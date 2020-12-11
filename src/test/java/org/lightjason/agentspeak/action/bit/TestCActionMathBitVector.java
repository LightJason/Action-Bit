/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package org.lightjason.agentspeak.action.bit;

import cern.colt.matrix.tbit.BitVector;
import cern.colt.matrix.tdouble.DoubleMatrix1D;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.action.bit.vector.CAnd;
import org.lightjason.agentspeak.action.bit.vector.CBoolValue;
import org.lightjason.agentspeak.action.bit.vector.CClear;
import org.lightjason.agentspeak.action.bit.vector.CCopy;
import org.lightjason.agentspeak.action.bit.vector.CCreate;
import org.lightjason.agentspeak.action.bit.vector.CFalseCount;
import org.lightjason.agentspeak.action.bit.vector.CHammingDistance;
import org.lightjason.agentspeak.action.bit.vector.CLambdaStreaming;
import org.lightjason.agentspeak.action.bit.vector.CNAnd;
import org.lightjason.agentspeak.action.bit.vector.CNot;
import org.lightjason.agentspeak.action.bit.vector.CNumericValue;
import org.lightjason.agentspeak.action.bit.vector.COr;
import org.lightjason.agentspeak.action.bit.vector.CRange;
import org.lightjason.agentspeak.action.bit.vector.CSet;
import org.lightjason.agentspeak.action.bit.vector.CSize;
import org.lightjason.agentspeak.action.bit.vector.CToBlas;
import org.lightjason.agentspeak.action.bit.vector.CToList;
import org.lightjason.agentspeak.action.bit.vector.CTrueCount;
import org.lightjason.agentspeak.action.bit.vector.CXor;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test for bit vector actions
 */
@TestInstance( TestInstance.Lifecycle.PER_CLASS )
public final class TestCActionMathBitVector extends IBaseTest
{
    /**
     * testing vector
     */
    private final BitVector m_vector1 = new BitVector( 3 );
    /**
     * testing matrix
     */
    private final BitVector m_vector2 = new BitVector( 3 );


    /**
     * initialize
     */
    @BeforeEach
    public void initialize()
    {
        m_vector1.put( 0, true );
        m_vector1.put( 1, false );
        m_vector1.put( 2, false );

        m_vector2.put( 0, false );
        m_vector2.put( 1, false );
        m_vector2.put( 2, true );
    }

    /**
     * data provider generator
     * @return data
     */
    public Stream<Arguments> generator()
    {
        return Stream.of(

                Arguments.of( Stream.of( m_vector1, m_vector2 ).map( CRawTerm::of ).collect( Collectors.toList() ), CFalseCount.class, Stream.of( 2D, 2D ) ),
                Arguments.of( Stream.of( m_vector1, m_vector2 ).map( CRawTerm::of ).collect( Collectors.toList() ), CCopy.class, Stream.of( m_vector1,
                                                                                                                                            m_vector2
                ) ),
                Arguments.of( Stream.of( m_vector1, m_vector2 ).map( CRawTerm::of ).collect( Collectors.toList() ), CTrueCount.class, Stream.of( 1D, 1D ) ),
                Arguments.of( Stream.of( m_vector1, m_vector2 ).map( CRawTerm::of ).collect( Collectors.toList() ), CSize.class, Stream.of( 3, 3 ) ),
                Arguments.of( Stream.of( m_vector1, m_vector2 ).map( CRawTerm::of ).collect( Collectors.toList() ), CNot.class, Stream.empty() ),
                Arguments.of( Stream.of( m_vector1, m_vector2 ).map( CRawTerm::of ).collect( Collectors.toList() ), COr.class, Stream.empty() ),
                Arguments.of( Stream.of( m_vector1, m_vector2 ).map( CRawTerm::of ).collect( Collectors.toList() ), CAnd.class, Stream.empty() ),
                Arguments.of( Stream.of( m_vector1, m_vector2 ).map( CRawTerm::of ).collect( Collectors.toList() ), CNAnd.class, Stream.empty() ),
                Arguments.of( Stream.of( m_vector1, m_vector2 ).map( CRawTerm::of ).collect( Collectors.toList() ), CHammingDistance.class, Stream.of( 2D ) ),
                Arguments.of( Stream.of( m_vector1, m_vector2 ).map( CRawTerm::of ).collect( Collectors.toList() ), CXor.class, Stream.empty() )

        );
    }

    /**
     * test all single-input actions
     *
     * @param p_input tripel of input data, actions classes and results
     * @throws IllegalAccessException is thrwon on instantiation error
     * @throws InstantiationException is thrwon on instantiation error
     * @throws NoSuchMethodException is thrwon on instantiation error
     * @throws InvocationTargetException is thrwon on instantiation error
     */
    @ParameterizedTest
    @MethodSource( "generator" )
    public void action( @Nonnull final List<ITerm> p_input, @Nonnull final Class<? extends IAction> p_action, @Nonnull final  Stream<Object> p_result )
        throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException
    {
        final List<ITerm> l_return = new ArrayList<>();

        p_action.getConstructor().newInstance().execute(
            false, IContext.EMPTYPLAN,
            p_input,
            l_return
        );

        Assertions.assertArrayEquals(
                p_result.toArray(),
                l_return.stream().map( ITerm::raw ).toArray(),
                p_action.toGenericString()
        );
    }

    /**
     * test create
     */
    @Test
    public void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 3 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertTrue( l_return.get( 0 ).raw() instanceof BitVector );
        Assertions.assertEquals( 3, l_return.get( 0 ).<BitVector>raw().size() );
    }

    /**
     * test boolean value
     */
    @Test
    public void boolValue()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CBoolValue().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( m_vector2, 0 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( false, l_return.get( 0 ).<Boolean>raw() );
    }

    /**
     * test set
     */
    @Test
    public void set()
    {
        final IExecution l_set = new CSet();

        l_set.execute(
            false, IContext.EMPTYPLAN,
            Stream.of( m_vector2, true, 0, 1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assertions.assertTrue( m_vector2.get( 0 ) );
        Assertions.assertTrue( m_vector2.get( 1 ) );

        l_set.execute(
            false, IContext.EMPTYPLAN,
            Stream.of( m_vector2, 0.5, 0, 1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assertions.assertFalse( m_vector2.get( 0 ) );
        Assertions.assertFalse( m_vector2.get( 1 ) );
    }

    /**
     * test clear
     */
    @Test
    public void clear()
    {
        new CClear().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( m_vector2, 0 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assertions.assertFalse( m_vector2.get( 0 ) );
    }

    /**
     * test range
     */
    @Test
    public void range()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CRange().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( m_vector2, 0, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertEquals( m_vector2, l_return.get( 0 ).<BitVector>raw() );
    }

    /**
     * test range error
     */
    @Test
    public void rangeerror()
    {
        Assertions.assertThrows(
            CExecutionIllegealArgumentException.class,
            () -> new CRange().execute(
                false, IContext.EMPTYPLAN,
                Collections.emptyList(),
                Collections.emptyList()
            )
        );
    }

    /**
     * test numericvalue
     */
    @Test
    public void numericvalue()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CNumericValue().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( m_vector1, 1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 0D, l_return.get( 0 ).<Number>raw() );
    }

    /**
     * test numericvalue error
     */
    @Test
    public void numericvalueerror()
    {
        Assertions.assertThrows(
            CExecutionIllegealArgumentException.class,
            () -> new CNumericValue().execute(
                false, IContext.EMPTYPLAN,
                Collections.emptyList(),
                Collections.emptyList()
            )
        );
    }

    /**
     * test toList
     */
    @Test
    public void tolist()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CToList().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( m_vector1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assertions.assertArrayEquals( Stream.of( 1D, 0D, 0D ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
    }

    /**
     * test toblas
     */
    @Test
    public void toblas()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final IExecution l_toblas = new CToBlas();

        l_toblas.execute(
            false, IContext.EMPTYPLAN,
            Stream.of( m_vector1, "dense" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        l_toblas.execute(
            false, IContext.EMPTYPLAN,
            Stream.of( m_vector2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 2, l_return.size() );
        Assertions.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix1D );
        Assertions.assertTrue( l_return.get( 1 ).raw() instanceof DoubleMatrix1D );

        Assertions.assertArrayEquals( Stream.of( 1, 0, 0 ).mapToDouble( i -> i ).toArray(), l_return.get( 0 ).<DoubleMatrix1D>raw().toArray(), 0 );
        Assertions.assertArrayEquals( Stream.of( 0, 0, 1 ).mapToDouble( i -> i ).toArray(), l_return.get( 1 ).<DoubleMatrix1D>raw().toArray(), 0 );
    }

    /**
     * test lambda streaming assignable
     */
    @Test
    public void lambdaassignable()
    {
        Assertions.assertTrue( new CLambdaStreaming().assignable().collect( Collectors.toSet() ).contains( BitVector.class ) );
    }

    /**
     * test lambda streaming
     */
    @Test
    public void lambda()
    {
        Assertions.assertArrayEquals(
            Stream.of( 1, 0, 0 ).toArray(),
            new CLambdaStreaming().apply( m_vector1 ).toArray()
        );
    }

    /**
     * test hamming distance
     */
    @Test
    public void hammingdistanceerror()
    {
        Assertions.assertThrows(
            CExecutionIllegealArgumentException.class,
            () -> new CHammingDistance().execute(
                false, IContext.EMPTYPLAN,
                Collections.emptyList(),
                Collections.emptyList()
            )
        );
    }

}
