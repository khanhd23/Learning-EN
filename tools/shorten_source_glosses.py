import json
from pathlib import Path
p=Path('content/en/words.json'); d=json.loads(p.read_text(encoding='utf-8'))
defs={'liaison_s1':'a secret or illicit sexual relationship','abusive_s1':'using cruel, insulting, or violent behavior','acting_s1':'temporarily doing the duties of a job','candied_s1':'covered or preserved with sugar or syrup','wedded_s1':'married or closely connected to an idea or purpose','amphibious_s1':'able to live or operate on land and in water','indemnify_s1':'to compensate for loss or financial responsibility'}
for word in d['words']:
 for sense in word.get('senses',[]):
  if sense.get('id') in defs: sense['def']=defs[sense['id']]
p.write_text(json.dumps(d,ensure_ascii=False,indent=2)+'\n',encoding='utf-8'); print(len(defs))
