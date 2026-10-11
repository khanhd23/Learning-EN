#!/usr/bin/env python3
"""Build original listening practice banks for the exam formats we support.

The scripts and questions in this file are authored practice material.  They are
not copied from an official paper.  The generated banks keep the official
question counts and part boundaries, while the Android engine presents the
supported multiple-choice form and keeps the audio script out of the active
answer view.
"""

from __future__ import annotations

import json
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "content" / "en" / "exams"


def rotate(values, offset):
    offset %= len(values)
    return values[offset:] + values[:offset]


def item(item_id, section, level, label, answer, pool, qtype, number, option_count=4):
    distractors = [v for v in pool if v != answer][: option_count - 1]
    options = [answer] + distractors
    order = (number * 7 + level) % option_count
    options = rotate(options, order)
    return {
        "id": item_id,
        "section": section,
        "level": level,
        "stem": f"What is the {label} mentioned in note {number}?",
        "opts": options,
        "ans": options.index(answer),
        "expl": f"The recording states that the {label} is {answer}.",
        "qtype": qtype,
    }


def group(group_id, section, set_id, title, intro, rows, qtype, level, option_count=4, context=""):
    values = [answer for _, answer, _ in rows]
    connectors = [
        "For your notes,",
        "The speaker then confirms that",
        "It is also important that",
        "The recording adds that",
        "Listeners should remember that",
    ]
    script = intro + (f" {context}" if context else "") + " " + " ".join(
        f"{connectors[index % len(connectors)]} the {label} is {answer}."
        for index, (label, answer, _) in enumerate(rows)
    )
    questions = []
    for number, (label, answer, pool) in enumerate(rows, 1):
        options_pool = [answer] + [v for v in pool if v != answer]
        questions.append(item(f"{group_id}_q{number:02d}", section, level, label, answer, options_pool, qtype, number, option_count))
    return {
        "id": group_id,
        "section": section,
        "set": str(set_id),
        "title": title,
        "passage": script,
        "audioPassage": script,
        "items": questions,
    }


TIME = ["8:15", "8:40", "9:05", "9:30", "10:20", "11:45"]
DAY = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]
ROOM = ["Room 104", "Room 208", "Room 315", "Studio B", "the north hall", "the library lobby"]
PLACE = ["Riverside Centre", "Oak Street", "Greenway Station", "Harbour Road", "Hilltop Library", "Central Market"]
PERSON = ["Mina Patel", "Jon Bell", "Sara Wong", "Leo Martin", "Nora Evans", "Owen Reed"]
NUMBER = ["12", "18", "24", "36", "48", "sixty-two"]
MONEY = ["18 dollars", "24 dollars", "30 dollars", "36 dollars", "42 dollars", "48 dollars"]
MONTH = ["12 April", "18 May", "24 June", "6 July", "15 August", "21 September"]
TOPIC = ["urban trees", "recycled glass", "sleep patterns", "coastal birds", "community kitchens", "museum maps"]


def rows_for(kind, set_id, group_index, count):
    shift = (set_id * 2 + group_index) % 6
    if kind == "ielts_p1":
        labels = ["visitor's name", "course", "starting day", "starting time", "fee", "meeting place", "room", "contact number", "email ending", "item to bring"]
        pools = [PERSON, ["evening photography", "beginner pottery", "street gardening", "local history", "food writing", "map reading"], DAY, TIME, MONEY, PLACE, ROOM, ["0148 273 611", "0162 440 905", "0183 772 410", "0195 208 364", "0131 664 782", "0177 392 508"], ["study.org", "learn.net", "centre.edu", "mailbox.org", "class.net", "library.edu"], ["a notebook", "a water bottle", "a passport photo", "indoor shoes", "a pencil", "a folder"]]
    elif kind == "ielts_p2":
        labels = ["opening time", "last entry", "bus route", "ticket price", "quiet area", "guided tour day", "collection floor", "reservation method", "accessibility feature", "closing day"]
        pools = [TIME, TIME, ["Route 4", "Route 7", "Route 12", "Route 18", "Route 21", "Route 26"], MONEY, ROOM, DAY, ["ground floor", "first floor", "second floor", "third floor", "east wing", "west wing"], ["the website", "the phone line", "the front desk", "the visitor app", "the ticket machine", "the information desk"], ["a ramp", "a lift", "large-print labels", "a hearing loop", "a quiet entrance", "a rest bench"], DAY]
    elif kind == "ielts_p3":
        labels = ["project title", "research question", "sample size", "data source", "first meeting", "software", "draft deadline", "main concern", "supervisor", "next action"]
        pools = [["Night buses", "Shared gardens", "Repair cafés", "Open classrooms", "River sensors", "Local archives"], TOPIC, NUMBER, ["a survey", "field notes", "a public database", "interviews", "a weather log", "museum records"], DAY, ["Atlas", "Cedar", "Maple", "Orchid", "Pine", "Quartz"], MONTH, ["small sample", "missing dates", "unclear labels", "late replies", "noisy data", "limited access"], PERSON, ["check the ethics form", "email the participants", "clean the spreadsheet", "book the lab", "read two articles", "draw the map"]]
    else:
        labels = ["definition", "period studied", "first example", "main cause", "measured change", "research tool", "unexpected result", "practical use", "limitation", "conclusion"]
        pools = [["a shared resource", "a seasonal movement", "a local exchange", "a heat island", "a memory cue", "a sound pattern"], ["the early 1900s", "the 1950s", "the 1970s", "the 1990s", "the last decade", "the present day"], TOPIC, ["traffic growth", "dry weather", "new materials", "changed habits", "noise", "poor drainage"], ["a 12 percent rise", "a 9 percent fall", "a two-hour delay", "three extra visits", "a wider range", "a smaller gap"], ["a field diary", "a sound meter", "a satellite image", "a memory test", "a water gauge", "a map grid"], ["the pattern reversed", "the result was seasonal", "the oldest site improved", "the control group changed", "the samples clustered", "the final result was stable"], ["school planning", "public transport", "library design", "farm work", "health advice", "coastal management"], ["short observation", "small sample", "missing records", "one location", "weather changes", "limited funding"], ["more local trials", "longer observation", "better labels", "a second survey", "a public workshop", "a wider comparison"]]
    output = []
    for index in range(count):
        base_label = labels[index % len(labels)]
        label = base_label if index < len(labels) else f"{base_label} detail {index + 1}"
        pool = pools[index % len(pools)]
        values = rotate(pool, shift + index)
        answer = values[0]
        output.append((label, answer, values))
    return output


def build_ielts():
    groups = []
    for set_id in range(1, 4):
        for part in range(1, 5):
            kind = f"ielts_p{part}"
            section = f"listening{part}"
            title = ["Everyday arrangements", "A public-information talk", "A student project discussion", "An academic mini-lecture"][part - 1]
            intro = [
                "A learner phones a community centre to arrange a course.",
                "A guide explains changes to a city visitor centre.",
                "Two students meet their supervisor to plan a small study.",
                "A lecturer introduces a research topic and its practical limits.",
            ][part - 1]
            groups.append(group(f"ielts_listen_{set_id}_p{part}", section, set_id, title, intro, rows_for(kind, set_id, part, 10), f"ielts_listen_p{part}", 3 + (part > 2)))
    return {"items": [], "groups": groups}


def build_vstep():
    groups = []
    counts = (8, 12, 15)
    for set_id in range(1, 4):
        for part, count in enumerate(counts, 1):
            kind = "ielts_p1" if part == 1 else "ielts_p3" if part == 2 else "ielts_p4"
            section = f"listening{part}"
            intro = [
                "You will hear short announcements and instructions from everyday situations.",
                "You will hear a longer discussion in which the speakers compare plans and evidence.",
                "You will hear a talk in which a speaker develops an idea and gives examples.",
            ][part - 1]
            groups.append(group(f"vstep_listen_{set_id}_p{part}", section, set_id, f"VSTEP Listening Part {part}", intro, rows_for(kind, set_id, part + 4, count), f"vstep_listen_p{part}", 3 + (part > 1)))
    return {"items": [], "groups": groups}


def cambridge_rows(set_id, part, count, level):
    pools = [
        ["the café", "the sports centre", "the bus station", "the museum", "the bookshop", "the park"],
        ["Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
        ["a blue bag", "a red ticket", "a green key", "a white jacket", "a black umbrella", "a yellow notebook"],
        ["at 9:00", "at 9:30", "at 10:00", "at 10:30", "at 11:00", "at 11:30"],
        ["by train", "by bus", "by bicycle", "on foot", "by taxi", "by tram"],
    ]
    labels = ["place", "day", "item", "time", "travel method"]
    rows = []
    for i in range(count):
        pool_index = (i + part - 1) % len(pools)
        pool = rotate(pools[pool_index], set_id + i + part)
        rows.append((labels[pool_index], pool[0], pool))
    return rows


def build_ket():
    groups = []
    settings = [
        "the school office", "the town library", "the railway station", "the sports centre",
        "the community hall", "the art museum", "the campsite", "the swimming pool",
        "the music shop", "the town market", "the travel desk", "the college café", "the park office",
    ]
    purposes = [
        "a weekend visit", "a club meeting", "a school activity",
    ]
    counts = {1: [1, 1, 1, 1, 1], 2: [5], 3: [5], 4: [1, 1, 1, 1, 1], 5: [5]}
    scene_index = 0
    for set_id in range(1, 4):
        for part, group_counts in counts.items():
            for number, count in enumerate(group_counts, 1):
                section = f"listening{part}"
                intro = [
                    "A short everyday conversation gives one key detail.",
                    "A visitor records notes while a guide describes a place.",
                    "Two friends discuss a simple plan and check five details.",
                    "A short announcement gives the main message.",
                    "Two speakers compare arrangements and agree on information.",
                ][part - 1]
                rows = cambridge_rows(set_id + number - 1, part, count, 2)
                context = f"The speakers are checking {purposes[scene_index % len(purposes)]} at {settings[scene_index // len(purposes)]}."
                groups.append(group(f"ket_listen_{set_id}_p{part}_{number:02d}", section, set_id, f"A2 Key Listening Part {part}", intro, rows, f"a2_listen_p{part}", 2, option_count=3, context=context))
                scene_index += 1
    return {"items": [], "groups": groups}


def build_pet():
    groups = []
    # B1 Preliminary Listening has four parts and 25 questions.  The app uses
    # 7/6/6/6 items, preserving the total and the four-part progression.
    counts = (7, 6, 6, 6)
    for set_id in range(1, 4):
        for part, count in enumerate(counts, 1):
            intro = [
                "You will hear short everyday exchanges and identify the key detail.",
                "You will hear a longer conversation and follow the speakers' plans.",
                "You will hear a short interview and identify the speaker's opinion.",
                "You will hear a short talk and identify its main message.",
            ][part - 1]
            groups.append(group(f"pet_listen_{set_id}_p{part}", f"listening{part}", set_id, f"B1 Preliminary Listening Part {part}", intro, cambridge_rows(set_id + 2, part, count, 3), f"b1_listen_p{part}", 3, option_count=3))
    return {"items": [], "groups": groups}


def write(name, value):
    (OUT / name).write_text(json.dumps(value, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")


def main():
    OUT.mkdir(parents=True, exist_ok=True)
    write("ielts_listening.json", build_ielts())
    write("vstep_listening.json", build_vstep())
    write("ket_listening.json", build_ket())
    write("pet_listening.json", build_pet())


if __name__ == "__main__":
    main()
