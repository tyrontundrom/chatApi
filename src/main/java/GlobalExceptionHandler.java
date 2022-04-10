import lombok.extern.slf4j.Slf4j;

@Slf4j
class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error(e.toString());
        log.error(t.getName());
        e.printStackTrace();
    }
}
