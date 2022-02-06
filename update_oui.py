import datetime
from pathlib import Path
import requests

OUI_CSV = "oui.csv"
OUI_MILLIS = "oui_date_millis.txt"

def get_raw_path() -> str:
    curdir = Path(__file__).parents[0]
    dir = curdir.joinpath("app", "src", "main", "res", "raw")
    return dir


def get_csv() -> tuple[str, str]:
    output = requests.get("http://standards-oui.ieee.org/oui/oui.csv")
    last_modified = output.headers["Last-Modified"]
    return (output.text, last_modified)


def update_csv(data: str):
    path = get_raw_path()
    file = path.joinpath(OUI_CSV)

    with open(file, 'w') as outfile:
        outfile.write(data)

def update_timestamp(str_date: str):
    path = get_raw_path()
    file = path.joinpath(OUI_MILLIS)

    print(f"Last-Modified: {str_date}")
    temp_time = datetime.datetime.strptime(str_date, "%a, %d %b %Y %H:%M:%S %Z").replace(tzinfo=datetime.timezone.utc)
    timestamp = temp_time.timestamp()
    timestamp_millis = str(round(timestamp * 1000))
    print(f"Timestamp: {timestamp_millis}")

    with open(file, 'w') as outfile:
        outfile.write(timestamp_millis)

def main():
    output = get_csv()
    update_csv(output[0])
    update_timestamp(output[1])

if __name__ == '__main__':
    main()