package com.jstarcraft.ai.neuralnetwork.loss;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;
import com.jstarcraft.ai.utility.MathUtility;

/**
 * Kullback Leibler Divergence loss function
 *
 * @author Susan Eraly
 */
/**
 * Binary XENT目标函数
 * 
 * <pre></pre>
 * 
 * @author Birdy
 *
 */
public class KLDLossFunction implements LossFunction {

	@Override
	public float computeScore(MathMatrix tests, MathMatrix trains, MathMatrix masks) {
		float score = 0F;
		for (MatrixScalar term : trains) {
			float value = term.getValue();
			value = value < MathUtility.EPSILON ? MathUtility.EPSILON : (value > 1F ? 1F : value);
			float label = tests.getValue(term.getRow(), term.getColumn());
			label = label < MathUtility.EPSILON ? MathUtility.EPSILON : (label > 1F ? 1F : label);
			float ratio = (float) FastMath.log(label / value);
			score += ratio * label;
		}

		// TODO 暂时不处理masks
		return score;
	}

	@Override
	public void computeGradient(MathMatrix tests, MathMatrix trains, MathMatrix masks, MathMatrix gradients) {
		gradients.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = trains.getValue(row, column);
			value = -(tests.getValue(row, column) / value);
			scalar.setValue(value);
		});
		// TODO 暂时不处理masks
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		if (getClass() != object.getClass()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return "KLDLossFunction()";
	}

}
