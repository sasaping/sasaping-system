# 점검할 포트 목록
# EX) PORTS=(19091 19092)
PORTS=(19091 19061)

for PORT in "${PORTS[@]}"; do
  # 포트가 사용 중인지 확인
  PID=$(lsof -t -i :$PORT)

  # 포트가 사용 중이면 프로세스 종료
  if [ -n "$PID" ]; then
    echo "포트 $PORT 사용 중. PID: $PID. 프로세스 종료 중..."
    kill -9 $PID
    echo "PID $PID 종료 완료."
  else
    echo "포트 $PORT 사용 가능."
  fi
done
