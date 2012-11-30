package edu.neu.madcourse.binbo.rocketrush.speech;


public class NativeOpus {
	
	public static final String TAG = "NativeOpus";
	
	/** Best for most VoIP or video conference applications
	 *  where listening quality and intelligibility matter most
	 */
	public static final int OPUS_APPLICATION_VOIP  = 2048;
	/** Best for broadcast/high-fidelity application where the 
	 * decoded audio should be as close as possible to the input
	 */
	public static final int OPUS_APPLICATION_AUDIO = 2049;
	/** Only use when lowest-achievable latency is what matters most. 
	 * Voice-optimized modes cannot be used.
	 */
	public static final int OPUS_APPLICATION_RESTRICTED_LOWDELAY = 2051;	
	
	// parameter id for encoder
	public static final int PARAM_ENCODER_SAMPLERATE     = 0;
	public static final int PARAM_ENCODER_CHANNELS       = 1;
	public static final int PARAM_ENCODER_BITRATE_BPS    = 2;
	public static final int PARAM_ENCODER_BANDWIDTH      = 3;
	public static final int PARAM_ENCODER_VBR            = 4;
	public static final int PARAM_ENCODER_VBR_CONSTRAINT = 5;
	public static final int PARAM_ENCODER_COMPLEXITY     = 6;
	public static final int PARAM_ENCODER_INBAND_FEC     = 7;
	public static final int PARAM_ENCODER_FORCE_CHANNELS = 8;
	public static final int PARAM_ENCODER_DTX			 = 9;
	public static final int PARAM_ENCODER_PACKET_LOSS    = 10;
	// parameter id for decoder
	public static final int PARAM_DECODER_SAMPLERATE     = 50;
	public static final int PARAM_DECODER_CHANNELS       = 51;
	public static final int PARAM_DECODER_SAMPLEFORMAT   = 52;
	public static final int PARAM_DECODER_BITRATE		 = 53;
	
	// supported sample format
	public static final int OPUS_SAMPLE_FMT_U8  = 0;
	public static final int OPUS_SAMPLE_FMT_S16 = 1;

	protected OnConvertProgressListener mConvertProgressListener = null;
	protected OnEncounterErrorListener  mEncounterErrorListener  = null;
	
	static {
		System.loadLibrary("ffmpeg-6");
		System.loadLibrary("opus-driver-6");		
		nativeInit();
    }
	
	public NativeOpus() {	
	}

	public void setConvertProgressListener(OnConvertProgressListener listener) {
		mConvertProgressListener = listener;
	}

	protected void updateConvertProgress(int progress) {
		if (mConvertProgressListener != null) {
			mConvertProgressListener.onUpdateConvertProgress(progress);
		}
	}
	
	public static final int ERR_NO_FILE_EXISTED = 0;
	
	protected void encounterError(int errCode) {
		if (mEncounterErrorListener != null) {
			mEncounterErrorListener.onEncounterOpusError(errCode);
		}
	}

	protected native static int nativeInit();
	
	public native int createEncoder(
		int bitrate,
		int samplerate, 
		int channels, 
		int application, 
		String filepath
	);
	public native int createDecoder(String filepath);
	public native int destroyEncoder();
	public native int destroyDecoder();
    public native int encode(byte[] buffer, int size);
    public native int decode(byte[] buffer, int size);
    public native int convertFile(String from, String to);
    public native int cancelConverting();
    public native int setParameter(int id, Object param);
    public native Object getParameter(int id);

    public interface OnConvertProgressListener {
    	void onUpdateConvertProgress(int progress);
    }
    
    public interface OnEncounterErrorListener {
    	void onEncounterOpusError(int errCode);
    }
}
