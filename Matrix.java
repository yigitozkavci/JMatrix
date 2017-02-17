public class Matrix {
	private int nrows;
	private int ncols;
	private double[][] data;

	public Matrix(double[][] dat) {
		this.data = dat;
		this.nrows = dat.length;
		this.ncols = dat[0].length;
	}

	public Matrix(int nrow, int ncol) {
		this.nrows = nrow;
		this.ncols = ncol;
		data = new double[nrow][ncol];
	}

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

	public double[][] getValues() {
		return data;
	}

	public void setValues(double[][] values) {
		this.data = values;
	}

	public void setValueAt(int row, int col, double value) {
		data[row][col] = value;
	}

	public double getValueAt(int row, int col) {
		return data[row][col];
	}

	public boolean isSquare() {
		return nrows == ncols;
	}

	public int size() {
		if (isSquare())
			return nrows;
		return -1;
	}

	public Matrix multiplyByConstant(double constant) {
		Matrix mat = new Matrix(nrows, ncols);
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				mat.setValueAt(i, j, data[i][j] * constant);
			}
		}
		return mat;
	}

	public Matrix insertColumnWithValue1() {
		Matrix X_ = new Matrix(this.getNrows(), this.getNcols()+1);
		for (int i=0;i<X_.getNrows();i++) {
			for (int j=0;j<X_.getNcols();j++) {
				if (j==0)
					X_.setValueAt(i, j, 1.0);
				else 
					X_.setValueAt(i, j, this.getValueAt(i, j-1));
				
			}
		}
		return X_;
	}

	public static Matrix transpose(Matrix matrix) {
	    Matrix transposedMatrix = new Matrix(matrix.getNcols(), matrix.getNrows());
	    for (int i=0;i<matrix.getNrows();i++) {
	        for (int j=0;j<matrix.getNcols();j++) {
	            transposedMatrix.setValueAt(j, i, matrix.getValueAt(i, j));
	        }
	    }
	    return transposedMatrix;
	} 

	private static int changeSign(int val) {
		return val % 2 == 0 ? 1 : -1;
	}

	public static double determinant(Matrix matrix) {
	    if (matrix.size() == 1) {
		return matrix.getValueAt(0, 0);
	    }
	    if (matrix.size()==2) {
	        return (matrix.getValueAt(0, 0) * matrix.getValueAt(1, 1)) - ( matrix.getValueAt(0, 1) * matrix.getValueAt(1, 0));
	    }
	    double sum = 0.0;
	    for (int i=0; i<matrix.getNcols(); i++) {
	        sum += changeSign(i) * matrix.getValueAt(0, i) * determinant(createSubMatrix(matrix, 0, i));
	    }
	    return sum;
	}

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
	
	public double[] getRow(int row) {
		return this.data[row];
	}

	public static Matrix createSubMatrix(Matrix matrix, int excluding_row, int excluding_col) {
	    Matrix mat = new Matrix(matrix.getNrows()-1, matrix.getNcols()-1);
	    int r = -1;
	    for (int i=0;i<matrix.getNrows();i++) {
	        if (i==excluding_row)
	            continue;
	            r++;
	            int c = -1;
	        for (int j=0;j<matrix.getNcols();j++) {
	            if (j==excluding_col)
	                continue;
	            mat.setValueAt(r, ++c, matrix.getValueAt(i, j));
	        }
	    }
	    return mat;
	} 

	public static Matrix cofactor(Matrix matrix) {
	    Matrix mat = new Matrix(matrix.getNrows(), matrix.getNcols());
	    for (int i=0;i<matrix.getNrows();i++) {
	        for (int j=0; j<matrix.getNcols();j++) {
	            mat.setValueAt(i, j, changeSign(i) * changeSign(j) * determinant(createSubMatrix(matrix, i, j)));
	        }
	    }
	    
	    return mat;
	}

	public static Matrix inverse(Matrix matrix) {
	    return (transpose(cofactor(matrix)).multiplyByConstant(1.0/determinant(matrix)));
	}

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

	public static Matrix identity(int dim) {
		Matrix result = new Matrix(dim, dim);
		for(int i = 0; i < dim; i++) {
			result.setValueAt(i, i, 1);
		}
		return result;
	}

	public Matrix mul(Matrix m) {
		Matrix result = new Matrix(this.getNrows(), m.getNcols());
		for(int i = 0; i < this.getNrows(); i++) {
			for(int j = 0; j < m.getNcols(); j++) {
				for(int k = 0; k < this.getNcols(); k++) {
					double val = this.getValueAt(i, k) * m.getValueAt(k, j);
					result.setValueAt(i, j, val + result.getValueAt(i, j));
				}
			}
		}
		return result;
	}

	public Matrix sub(Matrix m) {
		Matrix result = new Matrix(this.getNrows(), this.getNcols());
		for(int i = 0; i < this.getNrows(); i++) {
			for(int j = 0; j < this.getNcols(); j++) {
				result.setValueAt(i, j, this.getValueAt(i, j) - m.getValueAt(i, j));
			}
		}
		return result;
	}
}
