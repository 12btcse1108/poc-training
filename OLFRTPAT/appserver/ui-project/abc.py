import psutil
PROCNAME = "DataGenStockTick"
for proc in psutil.process_iter():
    print(proc)