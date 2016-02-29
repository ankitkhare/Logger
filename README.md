# Logger

Simple utility class to log messages on system and/or device storage, and show toast notifications. Just download/copy and paste it in the <code>utils</code> folder and start using.

Steps to use:
  1. Initialize the utility by calling <code>Logger.init()</code> method.
  2. Pass <code>true</code> as 2nd parameter if application can be debugged.
  3. Pass <code>true</code> as 3rd parameter if log file must be created on disk.
  4. Call <code>debug()</code>, <code>info()</code>, or <code>warn()</code> methods for logging & <code>toastLong()</code> or <code>toastShort()</code> methods to show toasts.

Note:
  - Toasts cannot be printed in files.
  - This uses separate thread to write messages in file.
  - Current max cap. for log file is <strong>1MB</strong> (preferred) but can be changed if needed.
  - Log file can be accessed using any FileManager at path : <em>/Android/data/(application_package)/files/Logs.txt</em>
  - <strong>INFO</strong> logs can only be printed on system or file if <em>application is debuggable</em> (2nd param of <code>init()</code> method), rest all types of logs are free to be used.
