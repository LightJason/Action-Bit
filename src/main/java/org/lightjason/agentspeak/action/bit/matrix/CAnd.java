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

package org.lightjason.agentspeak.action.bit.matrix;

import cern.colt.matrix.tbit.BitMatrix;
import org.lightjason.agentspeak.common.IPath;

import javax.annotation.Nonnull;


/**
 * performs the logical and operation to all bit matrices.
 * The action runs the logical and operator, the first
 * argument is the bit matrix, that is combined with
 * all other bit matrices, so \f$ m_i = m_i \text{ && } m_1 \f$
 * is performed, the action never fails
 *
 * {@code .math/bit/matrix/and( Matrix, Matrix1, Matrix2 );}
 */
public final class CAnd extends IBaseOperator
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 2066423986360578344L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CAnd.class, "math", "bit", "matrix" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Override
    protected void apply( @Nonnull final BitMatrix p_target, @Nonnull final BitMatrix p_source )
    {
        p_target.and( p_source );
    }

}
