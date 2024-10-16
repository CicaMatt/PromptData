import pandas as pd
from sklearn import preprocessing

# Sample DataFrame (expand as needed)
df = pd.DataFrame({
    'pets': ['cat', 'dog', 'cat', 'monkey', 'dog', 'dog'],
    'owner': ['Champ', 'Ron', 'Brick', 'Champ', 'Veronica', 'Ron'],
    'location': ['San_Diego', 'New_York', 'New_York', 'San_Diego', 'San_Diego', 'New_York']
})

# Reshape DataFrame into a single column
all_values = df.values.ravel()  # Flatten the DataFrame

# Fit LabelEncoder on all values
le = preprocessing.LabelEncoder()
le.fit(all_values)

# Transform the DataFrame
encoded_df = df.apply(le.transform)

print(encoded_df)