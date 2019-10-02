import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IGuildServerSettings, defaultValue } from 'app/shared/model/bot/guild-server-settings.model';

export const ACTION_TYPES = {
  FETCH_GUILDSERVERSETTINGS_LIST: 'guildServerSettings/FETCH_GUILDSERVERSETTINGS_LIST',
  FETCH_GUILDSERVERSETTINGS: 'guildServerSettings/FETCH_GUILDSERVERSETTINGS',
  CREATE_GUILDSERVERSETTINGS: 'guildServerSettings/CREATE_GUILDSERVERSETTINGS',
  UPDATE_GUILDSERVERSETTINGS: 'guildServerSettings/UPDATE_GUILDSERVERSETTINGS',
  DELETE_GUILDSERVERSETTINGS: 'guildServerSettings/DELETE_GUILDSERVERSETTINGS',
  RESET: 'guildServerSettings/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGuildServerSettings>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type GuildServerSettingsState = Readonly<typeof initialState>;

// Reducer

export default (state: GuildServerSettingsState = initialState, action): GuildServerSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GUILDSERVERSETTINGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GUILDSERVERSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GUILDSERVERSETTINGS):
    case REQUEST(ACTION_TYPES.UPDATE_GUILDSERVERSETTINGS):
    case REQUEST(ACTION_TYPES.DELETE_GUILDSERVERSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GUILDSERVERSETTINGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GUILDSERVERSETTINGS):
    case FAILURE(ACTION_TYPES.CREATE_GUILDSERVERSETTINGS):
    case FAILURE(ACTION_TYPES.UPDATE_GUILDSERVERSETTINGS):
    case FAILURE(ACTION_TYPES.DELETE_GUILDSERVERSETTINGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDSERVERSETTINGS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDSERVERSETTINGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GUILDSERVERSETTINGS):
    case SUCCESS(ACTION_TYPES.UPDATE_GUILDSERVERSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GUILDSERVERSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/bot/api/guild-server-settings';

// Actions

export const getEntities: ICrudGetAllAction<IGuildServerSettings> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_GUILDSERVERSETTINGS_LIST,
  payload: axios.get<IGuildServerSettings>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IGuildServerSettings> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GUILDSERVERSETTINGS,
    payload: axios.get<IGuildServerSettings>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGuildServerSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GUILDSERVERSETTINGS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGuildServerSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GUILDSERVERSETTINGS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGuildServerSettings> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GUILDSERVERSETTINGS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
