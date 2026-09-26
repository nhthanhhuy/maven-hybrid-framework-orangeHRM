package reportConfig;

import core.VerificationFailures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.Utils;

import java.util.List;

public class MethodListener implements IInvokedMethodListener {

    private static final Logger log = LogManager.getLogger(MethodListener.class);

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult result) {
        log.debug("Before invocation of: " + method.getTestMethod().getMethodName());
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        log.debug("After invocation of: " + method.getTestMethod().getMethodName());
        Reporter.setCurrentTestResult(result);

        if (method.isTestMethod()) {
            VerificationFailures allFailures = VerificationFailures.getFailures();

            // Thêm lỗi hiện tại nếu có
            if (result.getThrowable() != null) {
                allFailures.addFailureForTest(result, result.getThrowable());
            }

            List<Throwable> failures = allFailures.getFailuresForTest(result);
            int size = failures.size() - 1;

            if (size > 0) {
                result.setStatus(ITestResult.FAILURE);

                if (size == 1) {
                    result.setThrowable(failures.get(0));
                } else {
                    // Gộp nhiều lỗi thành 1 message
                    StringBuffer message = new StringBuffer("Multiple failures (")
                            .append(size).append("):\n");
                    for (int i = 0; i < size - 1; i++) {
                        message.append("Failure ").append(i + 1)
                                .append(" of ").append(size).append("\n");
                        message.append(Utils.longStackTrace(failures.get(i), false))
                                .append("\n");
                    }
                    Throwable last = failures.get(size - 1);
                    message.append("Failure ").append(size)
                            .append(" of ").append(size).append("\n");
                    message.append(last.toString());

                    Throwable merged = new Throwable(message.toString());
                    merged.setStackTrace(last.getStackTrace());
                    result.setThrowable(merged);
                }
            }
        }
    }
}