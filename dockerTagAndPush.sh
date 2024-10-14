# 모든 서비스 도커 이미지를 빌드합니다.
services=("eureka" "gateway" "user" "auth" "order" "payment" "slack" "product")

# 도커 이미지에 commit hash를 기반으로한 이미지 태그를 설정합니다.
commit_hash=$(git rev-parse --short HEAD)

for service in "${services[@]}"
do
  imageName="$ECR_REGISTRY/$ECR_NAMESPACE/$service"
  # 이미지를 구분하기 위해서 latest 이외의 태그를 추가합니다.
  docker tag "$imageName:latest" "$imageName:$commit_hash"

  # AWS ECR에 Push
  docker push "$imageName:latest"
  docker push "$imageName:$commit_hash"

  echo "$service image is built and pushed to AWS ECR"
done

echo "Build and Push processing is done"
