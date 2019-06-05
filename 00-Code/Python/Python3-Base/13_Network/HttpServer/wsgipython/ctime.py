import time


def application(env, start_response):
    # env.get("Method")
    # env.get("PATH_INFO")
    # env.get("QUERY_STRING")

    status = "200 OK"

    headers = [
        ("Content-Type", "text/plain")
    ]

    start_response(status, headers)

    return time.ctime()
