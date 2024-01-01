/*
 * This file is licensed under the MIT License, part of Just Enough Items.
 * Copyright (c) 2014-2015 mezz
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

package mezz.jei.api.fabric.constants;

import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import mezz.jei.api.fabric.ingredients.fluids.IJeiFluidIngredient;
import net.minecraft.world.level.material.Fluid;

/**
 * Built-in {@link IIngredientType} for Fabric Minecraft.
 *
 * @since 10.1.0
 */
public final class FabricTypes {
    /**
     * @since 10.1.0
     */
    public static final IIngredientTypeWithSubtypes<Fluid, IJeiFluidIngredient> FLUID_STACK = new IIngredientTypeWithSubtypes<>() {
        @Override
        public Class<? extends IJeiFluidIngredient> getIngredientClass() {
            return IJeiFluidIngredient.class;
        }

        @Override
        public Class<? extends Fluid> getIngredientBaseClass() {
            return Fluid.class;
        }

        @Override
        public Fluid getBase(IJeiFluidIngredient ingredient) {
            return ingredient.getFluid();
        }
    };

    private FabricTypes() {}
}
