package connection.entity;

/**
 * when response return byte[] stream
 * need to check the file type of current response
 * if type not match
 * change the 'typeMismatch' flag
 * 
 * @author Hammer
 *
 */
public class ByteFile {
	
	private byte[] stream;
	
	private boolean typeMismatch = false;

	public byte[] getStream() {
		return stream;
	}

	public void setStream(byte[] stream) {
		this.stream = stream;
	}

	public boolean isTypeMismatch() {
		return typeMismatch;
	}

	public void setTypeMismatch(boolean typeMismatch) {
		this.typeMismatch = typeMismatch;
	}
	
	
}
