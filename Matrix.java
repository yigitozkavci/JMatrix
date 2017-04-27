/**
 * A utility class for handling boilerplate code required
 * for simple matrix operations.
 * 
 * @author Yiğit Özkavcı
 * @version 1.0
 */
public class Matrix {
	private int nrows;
	private int ncols;
	private double[][] data;

	/**
	 * Constructor for converting a multidimensional array to
	 * Matrix class.
	 * 
	 * @param dat Provided multidimensional matrix
	 */
	public Matrix(double[][] dat) {
		this.data = dat;
		this.nrows = dat.length;
		this.ncols = dat[0].length;
	}

	/**
	 * Empty constructor with provided dimensions only.
	 * Usage:
	 * 
	 * Matrix matrix = new Matrix(3, 4); // Creates a 3 by 4 matrix
	 * @param nrow Number of rows
	 * @param ncol Number of columns
	 */
	public Matrix(int nrow, int ncol) {
		this.nrows = nrow;
		this.ncols = ncol;
		data = new double[nrow][ncol];
	}

	/**
	 * Displays this matrix in a fashionable way.
	 */
	public String toString() {
		String str = "";
		for(int i = 0; i < this.getNrows(); i++) {
			str += "|";
			for(int j = 0; j < this.getNcols(); j++) {
				str += this.data[i][j];
				if(j != this.getNcols() - 1) {
					str += ", ";
				}
			}
			str += "|\n";
		}
		return str;
	}

	/**
	 * Sets the value at specified row and column.
	 * 
	 * @param row Row at which value will be set
	 * @param col Column at which value will be set
	 * @param value Value to be set
	 */
	public void setValueAt(int row, int col, double value) {
		data[row][col] = value;
	}

	/**
	 * Gets the value at specified row and column.
	 * 
	 * @param row Row from which value will be retrieved
	 * @param col Column from which value will be retrieved
	 * @return Value at [row][col]
	 */
	public double getValueAt(int row, int col) {
		return data[row][col];
	}

	/**
	 * @return True if this is a square matrix, false otherwise.
	 */
	public boolean isSquare() {
		return nrows == ncols;
	}

	/**
	 * Basically (.*) operator in Matlab. Multiplies every element by
	 * given constant.
	 * 
	 * @param constant Constant with which this matrix's elements will be multiplied.
	 * @return A new matrix with every element multiplied with constant.
	 */
	public Matrix multiplyByConstant(double constant) {
		Matrix mat = new Matrix(nrows, ncols);
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				mat.setValueAt(i, j, data[i][j] * constant);
			}
		}
		return mat;
	}

	/**
	 * Basically (') operator in Matlab. Returns a transposed version of the matrix
	 * 
	 * @return Transposed version of this matrix
	 */
	public Matrix transpose() {
	    Matrix transposedMatrix = new Matrix(getNcols(), getNrows());
	    for (int i = 0; i < getNrows(); i++) {
	        for (int j = 0; j < getNcols(); j++) {
	            transposedMatrix.setValueAt(j, i, getValueAt(i, j));
	        }
	    }
	    return transposedMatrix;
	} 

	/**
	 * @return size of the matrix (ie. 4 if this is a 4x4 matrix).
	 * @throws Exception This method is callable only by square matrices.
	 */
	public int size() throws Exception {
		if(!isSquare()) {
			throw new Exception("Cannot call size() on a non-square matrix");
		}
		return this.nrows;
	}
	
	/**
	 * Recursively calculates the determinant of the matrix.
	 * 
	 * @return Determinant of the matrix.
	 * @throws Exception This methpd is callable only by square matrices.
	 */
	public double determinant() throws Exception {
		if(!isSquare()) {
			throw new Exception("Determinant of a non-square matrix is undefined.");
		}
	    if (size() == 1) {
		return getValueAt(0, 0);
	    }
	    if (size() == 2) {
	        return (getValueAt(0, 0) * getValueAt(1, 1)) - (getValueAt(0, 1) * getValueAt(1, 0));
	    }
	    double sum = 0.0;
	    for (int i = 0; i < getNcols(); i++) {
	        sum += changeSign(i) * getValueAt(0, i) * createSubMatrix(0, i).determinant();
	    }
	    return sum;
	}

	/**
	 * Cuts the given column from this matrix.
	 * 
	 * @param col Column to be cut
	 * @return A new version of this matrix where given column is cut.
	 */
	public Matrix cutCol(int col) {
		Matrix result = new Matrix(this.getNrows(), this.getNcols() - 1);
		for(int i = 0; i < this.getNrows(); i++) {
			for(int j = 0, k = 0; j < this.getNcols(); j++, k++) {
				if(j == col) {
					k--;
				} else {
					result.setValueAt(i, k, this.getValueAt(i, j));
				}
			}
		}
		return result;
	}

	/**
	 * Cuts the given row from this matrix.
	 * 
	 * @param row Row to be cut.
	 * @return A new version of this matrix where given row is cut.
	 */
	public Matrix cutRow(int row) {
		Matrix result = new Matrix(this.getNrows() - 1, this.getNcols());
		for(int i = 0, k = 0; i < this.getNrows(); i++, k++) {
			if(i == row) {
				k--;
			} else {
				for(int j = 0; j < this.getNcols(); j++) {
					result.setValueAt(k, j, this.getValueAt(i, j));
				}
			}
		}
		return result;
	}
	
	/**
	 * Retrieves the given row from this matrix.
	 * 
	 * @param row Row to be retrieved.
	 * @return Specified row data.
	 */
	public double[] getRow(int row) {
		return this.data[row];
	}

	/**
	 * This method is useful for calculating cofactors. Excluding one row
	 * and one column, returns a submatrix.
	 * 
	 * @param excluding_row Row to be excluded in the new matrix.
	 * @param excluding_col Column to be excluded in the new matrix.
	 * @return A new matrix with excluded row and column.
	 */
	public Matrix createSubMatrix(int excluding_row, int excluding_col) {
	    Matrix mat = new Matrix(getNrows()-1, getNcols()-1);
	    int r = -1;
	    for (int i = 0; i < getNrows(); i++) {
	        if (i == excluding_row)
	            continue;
	            r++;
	            int c = -1;
	        for (int j = 0; j < getNcols(); j++) {
	            if (j == excluding_col)
	                continue;
	            mat.setValueAt(r, ++c, getValueAt(i, j));
	        }
	    }
	    return mat;
	} 

	/**
	 * This is a helper function that acts according to "evenness" of the
	 * given integer. This method is not meant to be used publicly.
	 * 
	 * @param number Number to be checked.
	 * @return 1 if number is even, -1 otherwise.
	 */
	private int changeSign(int number) {
		return number % 2 == 0 ? 1 : -1;
	}
	
	/**
	 * @return Cofactor of this matrix
	 * @throws Exception Because this method makes use of determinant computation, also throws an exception.
	 */
	public Matrix cofactor() throws Exception {
	    Matrix mat = new Matrix(getNrows(), getNcols());
	    for (int i = 0; i<getNrows(); i++) {
	        for (int j = 0; j < getNcols(); j++) {
	            mat.setValueAt(i, j, changeSign(i) * changeSign(j) * createSubMatrix(i, j).determinant());
	        }
	    }
	    return mat;
	}

	/**
	 * Takes the inverse of this matrix.
	 * 
	 * @return Inverse of this matrix.
	 * @throws Exception
	 */
	public Matrix inverse() throws Exception {
	    return cofactor().transpose().multiplyByConstant(1.0/this.determinant());
	}

	/**
	 * Takes each row, calculates its sum and divides each value to that sum.
	 * Basically this method converts each row to probability format.
	 */
	public void convertToProbabilities() {
		for(int i = 0; i < this.data.length; i++) {
			int denom = 0;
			for(int j = 0; j < this.data[i].length; j++) {
				denom += this.data[i][j];
			}
			for(int j = 0; j < this.data[i].length; j++) {
				this.data[i][j] /= denom;
			}
		}
	}

	/**
	 * Returns an identity matrix by dim x dim.
	 * 
	 * @param dim Dimension of the identity matrix.
	 * @return Identity matrix with dimension dim.
	 */
	public static Matrix identity(int dim) {
		Matrix result = new Matrix(dim, dim);
		for(int i = 0; i < dim; i++) {
			result.setValueAt(i, i, 1);
		}
		return result;
	}

	/**
	 * Matrix multiplication.
	 * 
	 * @param matrix Matrix with which this class will be multiplied
	 * @return Result of (this * matrix)
	 */
	public Matrix mul(Matrix matrix) {
		Matrix result = new Matrix(this.getNrows(), matrix.getNcols());
		for(int i = 0; i < this.getNrows(); i++) {
			for(int j = 0; j < matrix.getNcols(); j++) {
				for(int k = 0; k < this.getNcols(); k++) {
					double val = this.getValueAt(i, k) * matrix.getValueAt(k, j);
					result.setValueAt(i, j, val + result.getValueAt(i, j));
				}
			}
		}
		return result;
	}

	/**
	 * Matrix subtraction
	 * 
	 * @param matrix Matrix that is going to be subtracted from this matrix
	 * @return Result of the subtraction
	 */
	public Matrix sub(Matrix matrix) {
		Matrix result = new Matrix(this.getNrows(), this.getNcols());
		for(int i = 0; i < this.getNrows(); i++) {
			for(int j = 0; j < this.getNcols(); j++) {
				result.setValueAt(i, j, this.getValueAt(i, j) - matrix.getValueAt(i, j));
			}
		}
		return result;
	}

	/**
	 * @return the current state of matrix as a multidimensional double array
	 */
	public double[][] getValues() {
		return data;
	}
	
	/**
	 * Overrides the current values of the matrix. Warning: this is not the
	 * best way of using this class. This data is meant to be mutated only by operations.
	 * 
	 * @param values Multidimensional array for overriding current values of the matrix.
	 */
	public void setValues(double[][] values) {
		this.data = values;
	}

	public int getNrows() {
		return nrows;
	}

	public void setNrows(int nrows) {
		this.nrows = nrows;
	}

	public int getNcols() {
		return ncols;
	}

	public void setNcols(int ncols) {
		this.ncols = ncols;
	}
}
