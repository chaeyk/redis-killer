This program kill AWS elasticache redis with high stress.

```shell script
$ docker run -it --rm -e KILLER_REDIS_ADDRESS={your redis address} chaeyk/redis-killer
```

Additional Options

You can set options with environment variables

| name                | type | description         |
|---------------------|:----:|---------------------|
| stress.thread-count | int  | thread count        |
| stress.data-count   | int  | test data count     |
